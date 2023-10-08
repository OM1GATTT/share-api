package top.om1ga.share.content.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.om1ga.share.common.resp.CommonResp;
import top.om1ga.share.content.domain.entity.Notice;
import top.om1ga.share.content.domain.entity.Share;
import top.om1ga.share.content.service.NoticeService;
import top.om1ga.share.content.service.ShareService;

import java.util.List;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月08日 15:03
 * @description ShareController
 */
@RestController
@RequestMapping(value = "/share")
@Slf4j
public class ShareController {

    @Resource
    private NoticeService noticeService;

    @Resource
    private ShareService shareService;

    @GetMapping("/notice")
    public CommonResp<Notice> getLatestNotice(){
        CommonResp<Notice> commonResp = new CommonResp<>();
        commonResp.setData(noticeService.getLatest());
        return commonResp;
    }

    @GetMapping("/list")
    public CommonResp<List<Share>> getShareList(@RequestParam(required = false) String title){
        CommonResp<List<Share>> commonResp = new CommonResp<>();
        Long userId = 2L;
        commonResp.setData(shareService.getList(title,userId));
        return commonResp;
    }
}
