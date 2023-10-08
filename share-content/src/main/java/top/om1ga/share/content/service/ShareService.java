package top.om1ga.share.content.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.util.StringUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.om1ga.share.content.domain.entity.MidUserShare;
import top.om1ga.share.content.domain.entity.Share;
import top.om1ga.share.content.mapper.MidUserShareMapper;
import top.om1ga.share.content.mapper.ShareMapper;

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
    private MidUserShareMapper midUserShareMapper;

    public List<Share> getList(String title,Long userId){
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
        // 执行按条件查询
        List<Share> shares = shareMapper.selectList(wrapper);

        // 处理后的 Share 数据列表
        List<Share> sharesDeal;

        // 1.如果用户为登录，那么 downloadUrl 全部设为 null
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
}