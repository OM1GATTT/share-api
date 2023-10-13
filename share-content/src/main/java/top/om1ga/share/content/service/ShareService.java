package top.om1ga.share.content.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.util.StringUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.om1ga.share.common.resp.CommonResp;
import top.om1ga.share.content.domain.dto.ExchangeDTO;
import top.om1ga.share.content.domain.dto.ShareRequestDTO;
import top.om1ga.share.content.domain.entity.MidUserShare;
import top.om1ga.share.content.domain.entity.Share;
import top.om1ga.share.content.domain.resp.ShareResp;
import top.om1ga.share.content.feign.User;
import top.om1ga.share.content.feign.UserAddBonusMsgDTO;
import top.om1ga.share.content.feign.UserService;
import top.om1ga.share.content.mapper.MidUserShareMapper;
import top.om1ga.share.content.mapper.ShareMapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月08日 20:33
 * @description ShareService
 */
@Service
public class ShareService {
    @Resource
    private ShareMapper shareMapper;

    @Resource
    private UserService userService;
    @Resource
    private MidUserShareMapper midUserShareMapper;

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
    public List<Share> querySharesNotYet(){
        LambdaQueryWrapper<Share> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Share::getId);
        wrapper.eq(Share::getShowFlag,false)
                .eq(Share::getAuditStatus,"NOT_YET");
        return shareMapper.selectList(wrapper);
    }
}
