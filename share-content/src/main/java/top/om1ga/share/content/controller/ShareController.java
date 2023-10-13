package top.om1ga.share.content.controller;

import cn.hutool.json.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import top.om1ga.share.common.resp.CommonResp;
import top.om1ga.share.common.util.JwtUtil;
import top.om1ga.share.content.domain.dto.ExchangeDTO;
import top.om1ga.share.content.domain.dto.ShareRequestDTO;
import top.om1ga.share.content.domain.entity.Notice;
import top.om1ga.share.content.domain.entity.Share;
import top.om1ga.share.content.domain.resp.ShareResp;
import top.om1ga.share.content.service.NoticeService;
import top.om1ga.share.content.service.ShareService;

import java.util.List;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月08日 15:03
 * @description ShareController
 */
@RefreshScope
@RestController
@RequestMapping(value = "/share")
@Slf4j
public class ShareController {

    @Value(value = "${systemUpdateNotice}")
    private Boolean systemUpdateNotice;

    @Resource
    private NoticeService noticeService;

    @Resource
    private ShareService shareService;

    private final int MAX = 100;


    @PostMapping("/contribute")
    public int contributeShare(@RequestBody ShareRequestDTO shareRequestDTO,@RequestHeader(value = "token",required = false)String token){
        long userId = getUserIdFromToken(token);
        shareRequestDTO.setUserId(userId);
        System.out.println(shareRequestDTO);
        return shareService.contribute(shareRequestDTO);
    }
    @GetMapping("/notice")
    public CommonResp<Notice> getLatestNotice(){
        System.out.println(systemUpdateNotice);
        CommonResp<Notice> commonResp = new CommonResp<>();
        if (systemUpdateNotice){
            commonResp.setData(Notice.builder().content("系统正在维护中...").build());
            return commonResp;
        }
        commonResp.setData(noticeService.getLatest());
        return commonResp;
    }

    @GetMapping("/list")
    public CommonResp<List<Share>> getShareList(@RequestParam(required = false) String title,
                                                @RequestParam(required = false,defaultValue = "1")Integer pageNo,
                                                @RequestParam(required = false,defaultValue = "3")Integer pageSize,
                                                @RequestHeader(value = "token",required = false) String token){
        if(pageSize>MAX){
            pageSize=MAX;
        }
        long userId = getUserIdFromToken(token);
        CommonResp<List<Share>> commonResp = new CommonResp<>();
        commonResp.setData(shareService.getList(title,pageNo,pageSize,userId));
        return commonResp;
    }

    private long getUserIdFromToken(String token){
        log.info(">>>>>>>>>token"+token);
        long userId=0;
        String noToken="no-token";
        if(!noToken.equals(token)){
            JSONObject jsonObject = JwtUtil.getJSONObject(token);
            log.info("解析到 token的JSON数据为:{}",jsonObject);
            userId=Long.parseLong(jsonObject.get("id").toString());
        }else {
            log.info("没有token");
        }
        return userId;
    }

    @GetMapping("/{id}")
    public CommonResp<ShareResp> getShareById(@PathVariable Long id){
        ShareResp shareResp = shareService.findById(id);
        CommonResp<ShareResp> commonResp = new CommonResp<>();
        commonResp.setData(shareResp);
        return commonResp;
    }

    @PostMapping("/exchange")
    public CommonResp<Share> exchange(@RequestBody ExchangeDTO exchangeDTO){
        System.out.println(exchangeDTO);
        CommonResp<Share> commonResp = new CommonResp<>();
        commonResp.setData(shareService.exchange(exchangeDTO));
        return commonResp;
    }
}
