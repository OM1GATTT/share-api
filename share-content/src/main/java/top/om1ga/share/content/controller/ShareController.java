package top.om1ga.share.content.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.om1ga.share.common.resp.CommonResp;
import top.om1ga.share.content.domain.entity.Notice;
import top.om1ga.share.content.service.NoticeService;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月08日 15:03
 * @description ShareController
 */
@RestController
@RequestMapping(value = "/share")
public class ShareController {

    @Resource
    private NoticeService noticeService;

    @GetMapping("/notice")
    public CommonResp<Notice> getLatestNotice(){
        CommonResp<Notice> commonResp = new CommonResp<>();
        commonResp.setData(noticeService.getLatest());
        return commonResp;
    }
}
