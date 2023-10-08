package top.om1ga.share.content.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.om1ga.share.content.domain.entity.Notice;
import top.om1ga.share.content.mapper.NoticeMapper;

import java.util.List;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月08日 14:58
 * @description NoticeService
 */
@Service
public class NoticeService {
    @Resource
    private NoticeMapper noticeMapper;

    public Notice getLatest(){
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        // 只查找 showFlag 字段 为 1 的
        wrapper.eq(Notice::getShowFlag,1);
        // 根据id 降序排序
        wrapper.orderByDesc(Notice::getId);
        List<Notice> noticeList = noticeMapper.selectList(wrapper);
        return noticeList.get(0);
    }
}
