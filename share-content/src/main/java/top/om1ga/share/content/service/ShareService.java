package top.om1ga.share.content.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.util.StringUtil;
import io.minio.ObjectWriteResponse;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.om1ga.share.common.resp.CommonResp;
import top.om1ga.share.content.config.MinioConfig;
import top.om1ga.share.content.domain.dto.ExchangeDTO;
import top.om1ga.share.content.domain.dto.ShareAuditDTO;
import top.om1ga.share.content.domain.dto.ShareRequestDTO;
import top.om1ga.share.content.domain.dto.UserAddBonusMQDTO;
import top.om1ga.share.content.domain.entity.MidUserShare;
import top.om1ga.share.content.domain.entity.Share;
import top.om1ga.share.content.domain.enums.AuditStatusEnum;
import top.om1ga.share.content.domain.resp.ShareResp;
import top.om1ga.share.content.feign.User;
import top.om1ga.share.content.feign.UserAddBonusMsgDTO;
import top.om1ga.share.content.feign.UserService;
import top.om1ga.share.content.mapper.MidUserShareMapper;
import top.om1ga.share.content.mapper.ShareMapper;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月08日 20:33
 * @description ShareService
 */
@Service
@AllArgsConstructor
public class ShareService {
    @Resource
    private ShareMapper shareMapper;
    @Resource
    private UserService userService;
    @Resource
    private MidUserShareMapper midUserShareMapper;
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    private MinioConfig minioConfig;

    private final String bucketName = "share-bucket";
    private final String directory = "avatar";


    public String uploadAvatar(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)){
            throw new RuntimeException();
        }
        String objectName = directory+ "/"+originalFilename;
        ObjectWriteResponse response;
        try {
            response = minioConfig.putObject(bucketName, objectName, file.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        System.out.println(response.headers());
        return minioConfig.getPreviewFileUrl(bucketName, objectName);
    }

    /**
     * 主页分享列表
     * @param title 查询参数-标题
     * @param pageNo 查询参数-页码
     * @param pageSize 查询参数-每页分享的个数
     * @param userId 查询参数-用户id
     * @return 分享列表
     */
    public List<Share> getList(String title,Integer pageNo,Integer pageSize,Long userId){
        // 构建查询条件
        LambdaQueryWrapper<Share> wrapper = new LambdaQueryWrapper<>();
        // 按照 id 降序查询所有数据
        wrapper.orderByDesc(Share::getId);
        // 如标题关键字不空，则加上模糊查询条件，否则结果即所有数据
        if (StringUtil.isNotEmpty(title)){
            wrapper.like(Share::getTitle,title);
        }
        // 过滤出所有已经通过审核的数据并需要显示的数据
        wrapper.eq(Share::getAuditStatus,"PASS").eq(Share::getShowFlag,true);

        // 内置的分页对象
        Page<Share> page = Page.of(pageNo,pageSize);
        // 执行按条件查询
        List<Share> shares = shareMapper.selectList(page,wrapper);

        // 处理后的 Share 数据列表
        List<Share> sharesDeal;

        // 1.如果用户未登录，那么 downloadUrl 全部设为 null
        if (userId == null){
            sharesDeal = shares.stream().peek(share -> share.setDownloadUrl(null)).collect(Collectors.toList());
        }else {
        //    2.如果用户登录了，那么查询 mid_user_share, 如果没有数据，那么这条 share 的 downloadUrl 也设为 null
        //    只有自己分享的资源才能直接看到下载链接，否则显示"兑换"
            sharesDeal = shares.stream().peek(share -> {
                MidUserShare midUserShare = midUserShareMapper.selectOne(new QueryWrapper<MidUserShare>().lambda()
                        .eq(MidUserShare::getUserId, userId)
                        .eq(MidUserShare::getShareId, share.getId()));
                if (midUserShare == null){
                    share.setDownloadUrl(null);
                }
            }).collect(Collectors.toList());
        }
        return sharesDeal;
    }

    public List<Share> getExchangedList(Integer pageNo,Integer pageSize,Long userId){
        // 构建查询条件
        LambdaQueryWrapper<Share> wrapper = new LambdaQueryWrapper<>();
        // 按照 id 降序查询所有数据
        wrapper.orderByDesc(Share::getId);
        // 过滤出所有已经通过审核的数据并需要显示的数据
        wrapper.eq(Share::getAuditStatus,"PASS").eq(Share::getShowFlag,true);

        // 内置的分页对象
        Page<Share> page = Page.of(pageNo,pageSize);
        // 执行按条件查询
        List<Share> shares = shareMapper.selectList(page,wrapper);

        // 处理后的 Share 数据列表
        List<Share> sharesDeal;

        // 1.如果用户未登录，那么 downloadUrl 全部设为 null
        if (userId == null){
            throw new IllegalArgumentException("用户未登录！");
        }else {
            //    2.如果用户登录了，那么查询 mid_user_share, 如果没有数据，那么移除这条 share
            //    只有自己分享的资源才能直接看到下载链接，否则显示"兑换"
            sharesDeal = shares.stream().filter(share -> {
                MidUserShare midUserShare = midUserShareMapper.selectOne(new QueryWrapper<MidUserShare>().lambda()
                        .eq(MidUserShare::getUserId, userId)
                        .eq(MidUserShare::getShareId, share.getId()));
                return midUserShare !=null;
            }).collect(Collectors.toList());
        }
        return sharesDeal;
    }

    public ShareResp findById(Long shareId){
        Share share = shareMapper.selectById(shareId);
        CommonResp<User> commonResp = userService.getUser(share.getUserId());
        return ShareResp.builder().share(share).nickname(commonResp.getData().getNickname()).avatarUrl(commonResp.getData().getAvatarUrl()).build();
    }

    public Share exchange(ExchangeDTO exchangeDTO){
        Long userId = exchangeDTO.getUserId();
        Long shareId = exchangeDTO.getShareId();
        // 1.根据 id 查询 share,校验需要兑换的资源是否存在
        Share share = shareMapper.selectById(shareId);
        if(share==null){
            throw new IllegalArgumentException("该分享不存在");
        }

        // 2.如果当前用户已经兑换过该分享，则直接返回该分享（不需要扣除积分）
        MidUserShare midUserShare = midUserShareMapper.selectOne(new QueryWrapper<MidUserShare>().lambda()
                .eq(MidUserShare::getUserId, userId)
                .eq(MidUserShare::getShareId, shareId)
        );
        if (midUserShare != null) {
            return share;
        }

        // 看用户积分是否足够
        CommonResp<User> commonResp = userService.getUser(userId);
        User user = commonResp.getData();
        Integer price = share.getPrice();
        if (price>user.getBonus()){
            throw new IllegalArgumentException("用户积分不够！");
        }

        // 修改积分（*-1 就是负值扣分）
        userService.updateBonus(UserAddBonusMsgDTO.builder().userId(userId).bonus(price*-1).build());
        // 向 mid_user_share 表插入一条数据，让这个用户对于这条资源拥有了下载权限
        midUserShareMapper.insert(MidUserShare.builder().userId(userId).shareId(shareId).build());
        return share;
    }

    public int contribute(ShareRequestDTO shareRequestDTO){
        Share share = Share.builder()
                .isOriginal(shareRequestDTO.getIsOriginal())
                .author(shareRequestDTO.getAuthor())
                .price(shareRequestDTO.getPrice())
                .downloadUrl(shareRequestDTO.getDownloadUrl())
                .summary(shareRequestDTO.getSummary())
                .buyCount(0)
                .title(shareRequestDTO.getTitle())
                .userId(shareRequestDTO.getUserId())
                .cover(shareRequestDTO.getCover())
                .createTime(new Date())
                .updateTime(new Date())
                .showFlag(false)
                .auditStatus("NOT_YET")
                .reason("未审核")
                .build();
        return shareMapper.insert(share);
    }

    public List<Share> myContribute(Integer pageNo,Integer pageSize,Long userId){
        LambdaQueryWrapper<Share> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Share::getUserId,userId);
        Page<Share> page = Page.of(pageNo, pageSize);
        return shareMapper.selectList(page,wrapper);
    }

    /**
     * 查询待审核状态的shares列表
     * @return List<Share>
     */
    public List<Share> querySharesNotYet(Integer pageNo,Integer pageSize,Long userId){
        CommonResp<User> user = userService.getUser(userId);
        if (!user.getData().getRoles().equals("admin")){
            throw new IllegalArgumentException("用户不是管理员账户，无访问权限");
        }
        LambdaQueryWrapper<Share> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Share::getId);
        wrapper.eq(Share::getShowFlag,false)
                .eq(Share::getAuditStatus,"NOT_YET");
        Page<Share> page = Page.of(pageNo, pageSize);
        return shareMapper.selectList(page,wrapper);

    }

    /**
     * 审核
     * @param id id
     * @param shareAuditDTO shareAuditDTO
     * @return Share
     */
    public Share auditById(Long id, ShareAuditDTO shareAuditDTO){
        Share share = shareMapper.selectById(id);
        if (share == null){
            throw new IllegalArgumentException("参数非法！该分享不存在");
        }
        if (!Objects.equals("NOT_YET",share.getAuditStatus())){
            throw new IllegalArgumentException("参数非法！该分享已审核通过或审核不通过");
        }
        share.setAuditStatus(shareAuditDTO.getAuditStatusEnum().toString());
        share.setReason(shareAuditDTO.getReason());
        share.setShowFlag(shareAuditDTO.getShowFlag());
        LambdaQueryWrapper<Share> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Share::getId,id);
        this.shareMapper.update(share,wrapper);

        this.midUserShareMapper.insert(
                MidUserShare.builder()
                        .userId(share.getUserId())
                        .shareId(id)
                        .build()
        );
        if (AuditStatusEnum.PASS.equals(shareAuditDTO.getAuditStatusEnum())){
            this.rocketMQTemplate.convertAndSend(
                    "add-bonus",
                    UserAddBonusMQDTO.builder()
                            .userId(share.getUserId())
                            .bonus(50)
                            .build()
            );
        }
        return share;
    }
}
