package cn.com.cnpc.cpoa.utils;

import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.bid.BidBiddingAttachDto;
import cn.com.cnpc.cpoa.domain.bid.BidBiddingDto;
import cn.com.cnpc.cpoa.enums.BiddingStatusEnum;
import cn.com.cnpc.cpoa.enums.FileOwnerTypeEnum;
import cn.com.cnpc.cpoa.mapper.BizAttachDtoMapper;
import cn.com.cnpc.cpoa.mapper.bid.BidBiddingAttachDtoMapper;
import cn.hutool.http.Header;
import cn.hutool.http.HttpGlobalConfig;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO   招标信息抓取
 * @date 2022/5/16 9:41
 */
public class BiddingUtils {

    private final long BEFORE_DAY = 3l;

    private final String bidListUrl;

    private final String bidDetailUrl;

    private final String bidAttachUrl;

    private final String attachPath;

    static final Pattern DATE_COMPILE = Pattern.compile("([0-9]{4}[年][0-9]{1,2}[月][0-9]{1,2}[日])", Pattern.CANON_EQ);

    static final Pattern YYYY_MM_DD = Pattern.compile("([0-9]{4}[-][0-9]{1,2}[-][0-9]{1,2})", Pattern.CANON_EQ);

    static final Pattern YUAN_COMPILE = Pattern.compile("([0-9]+元)", Pattern.CANON_EQ);

    static final Pattern WAN_YUAN_COMPILE = Pattern.compile("([0-9]+万元)", Pattern.CANON_EQ);

    static final Pattern YYYY_MM_DD_HH_MM = Pattern.compile("([0-9]{4}[年][0-9]{1,2}[月][0-9]{1,2}[日][0-9]{1,2}[时][0-9]{1,2}[分])", Pattern.CANON_EQ);

    static final String DETAIL_STATUS = "0";

    static final String LIST_STATUS = "1";

    public BiddingUtils(String bidListUrl, String bidDetailUrl, String bidAttachUrl, String attachPath) {
        this.bidListUrl = bidListUrl;
        this.bidDetailUrl = bidDetailUrl;
        this.bidAttachUrl = bidAttachUrl;
        this.attachPath = attachPath;
    }

    /**
     * 下载附件
     *
     * @param id       附件id
     * @param fileName
     */
    private HashMap<String, String> downLoadAttach(String id, String fileName) {
        HashMap<String, String> hashMap = new HashMap<>(16);
        try {
            URL url = new URL(bidAttachUrl + id + "&skip=true");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
            //得到输入流
            InputStream inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);
            int length = getData.length;
            String path = attachPath + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "/";
            File pathFile = new File(path);
            if (!pathFile.exists()) {
                pathFile.mkdir();
            }
            hashMap.put("size", String.valueOf(length));
            hashMap.put("path", path + fileName);
            //文件保存位置
            File file = new File(path + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 获取招标信息id集合
     *
     * @return
     */
    public List<String> getBidDataIds(String title) {
        String post = urlCall(LIST_STATUS, "", title);
        HashMap map = JSONObject.parseObject(post, HashMap.class);
        List list = (List) map.get("list");
        ArrayList<String> ids = new ArrayList<>();
        for (Object o : list) {
            JSONObject jsonObject = (JSONObject) o;
            HashMap<String, String> hashMap = new HashMap<>(16);
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                hashMap.put(entry.getKey(), (String) entry.getValue());
            }
            String dateTime = hashMap.get("dateTime");
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            //发布时间
            LocalDate release = LocalDate.parse(dateTime, pattern);
            LocalDate now = LocalDate.now();
            //3天前的时间
            LocalDate minusDays = now.minusDays(BEFORE_DAY);
            //如果发布时间在3天以前则不取
            if (!release.isBefore(minusDays)) {
                ids.add(hashMap.get("id"));
            } else {
                break;
            }
        }
        return ids;
    }

    /**
     * 接口调用
     *
     * @param status 1为获取招标集合，0为招标详情信息
     * @return
     */
    private String urlCall(String status, String id, String title) {
        JSONObject object = new JSONObject();
        object.put("categoryId", "199");
        object.put("pageNo", 1);
        object.put("pageSize", 15);
        object.put("pid", "198");
        object.put("projectType", "");
        object.put("title", title);
        object.put("url", "./page.html");
        String url;
        if (DETAIL_STATUS.equals(status)) {
            object.put("dataId", id);
            url = bidDetailUrl;
        } else {
            url = bidListUrl;
        }
        String post = HttpRequest.post(url)
                .header(Header.ACCEPT, "*/*")
                .header(Header.ACCEPT_ENCODING, "gzip, deflate, br")
                .header(Header.ACCEPT_LANGUAGE, "zh-CN")
                .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36")
                .header(Header.CONNECTION, "keep-alive")
                .header(Header.CONTENT_TYPE, "application/json")
                .header(Header.CONTENT_LENGTH, "140")
                .body(object.toJSONString())
                .timeout(HttpGlobalConfig.getTimeout())
                .execute().body();
        return post;
    }

    /**
     * 获取招标具体信息
     *
     * @param id
     * @return
     */
    public BidBiddingDto getBidData(String id, String title, BizAttachDtoMapper attachDtoMapper, BidBiddingAttachDtoMapper biddingAttachDtoMapper, List<String> projNos) {
        String post = urlCall(DETAIL_STATUS, id, title);
        try {
            Thread.sleep(2000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap map = JSONObject.parseObject(post, HashMap.class);
        List<JSONObject> list = (List) map.get("list");
        HashMap<String, String> hashMap = jsonObjToMap(list.get(0));
        String projNo = hashMap.get("projectcode");
        if (projNos.contains(projNo)) {
            return null;
        }
        String projName = hashMap.get("projectname");
        String publishAt = hashMap.get("dateTime");
        String noticeName = hashMap.get("noticename");
        String sourceHtml = hashMap.get("bulletincontent");
        if (StringUtils.isEmpty(sourceHtml)) {
            return null;
        }
        BidBiddingDto biddingDto = BidBiddingDto.builder().build();
        biddingDto.setKeyWord(title);
        String bidId = StringUtils.getUuid32();
        biddingDto.setBiddingId(bidId);
        biddingDto.setCrawlMethod("Net");
        biddingDto.setProjName(projName);
        biddingDto.setProjNo(projNo);
        biddingDto.setPublishAt(DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, publishAt));
        biddingDto.setNoticeName(noticeName);
        biddingDto.setSourceHtml(sourceHtml);
        biddingDto.setSourceUrl(bidDetailUrl);
        biddingDto.setCrawlAt(DateUtils.getNowDate());
        biddingDto.setBiddingStatus(BiddingStatusEnum.GETBIDING.getKey());
        String s = sourceHtml.replaceAll("(<[a-z]+[0-9]?.?>|<[a-z]+.+?>|</[a-z]+[0-9]?>|&[a-z]+;|<[a-z]+/>|style=\".+?\")", "")
                .replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "")
                .replaceAll(" ", "");
        String[] split = s.split("\n");
        //招标信息解析
        analysisBidData(biddingDto, split);

        //附件处理
        List<JSONObject> attachmentList = (List) map.get("attachmentList");
        attachProcess(attachmentList, bidId, attachDtoMapper, biddingAttachDtoMapper);

        return biddingDto;
    }

    /**
     * 招标信息解析
     *
     * @param biddingDto
     * @param split
     */
    private void analysisBidData(BidBiddingDto biddingDto, String[] split) {
        String biddingConditions = null;
        String projDesc = null;
        String getBidDocStartAt = null;
        String getBidDocEndAt = null;
        String guaranteeAmount = null;
        String bidOpenAt = null;
        String bidderFileAmount = null;
        for (int i = 0; i < split.length; i++) {
            String s1 = split[i];
            //招标条件
            if (Pattern.matches(".{2}招标条件$", s1)) {
                StringBuffer sb = new StringBuffer();
                for (int j = i; j < split.length; j++) {
                    String s2 = split[j];
                    if (Pattern.matches(".{2}项目概况与招标范围$", s2)) {
                        break;
                    } else {
                        sb.append(s2).append("\n");
                    }
                }
                biddingConditions = sb.toString();
                biddingDto.setBiddingConditions(biddingConditions);
            }
            //项目概况
            if (Pattern.matches(".{2}项目概况与招标范围$", s1)) {
                StringBuffer sb = new StringBuffer();
                for (int j = i; j < split.length; j++) {
                    String s2 = split[j];
                    if (Pattern.matches(".{2}投资人资格要求$", s2) || Pattern.matches(".{2}投标人资格要求$", s2) || Pattern.matches(".{2}投标人资格条件$", s2)) {
                        break;
                    } else {
                        sb.append(s2).append("\n");
                    }
                }
                projDesc = sb.toString();
                biddingDto.setProjDesc(projDesc);
            }
            //投资人资格要求
            if (Pattern.matches(".{2}投资人资格要求$", s1) || Pattern.matches(".{2}投标人资格要求$", s1) || Pattern.matches(".{2}投标人资格条件$", s1)) {
                StringBuffer sb = new StringBuffer();
                for (int j = i; j < split.length; j++) {
                    String s2 = split[j];
                    if (Pattern.matches(".{2}招标文件的获取$", s2) || Pattern.matches(".{2}招标文件的发售与获取$", s2)) {
                        break;
                    } else {
                        sb.append(s2).append("\n");
                    }
                }
                String bidderQualification = sb.toString();
                biddingDto.setBidderQualification(bidderQualification);
            }

            if (Pattern.matches(".{2}招标文件的获取$", s1) || Pattern.matches(".{2}招标文件的发售与获取$", s1)) {
                //招标文件的获取
                StringBuffer sb = new StringBuffer();
                for (int j = i; j < split.length; j++) {
                    String s2 = split[j];

                    //购买招标文件费用
                    if (s2.contains("招标文件售价") || s2.contains("招标文件每标段") || s2.contains("招标文件每套")) {
                        Matcher matcher = YUAN_COMPILE.matcher(s2);
                        while (matcher.find()) {
                            bidderFileAmount = matcher.group(0);
                        }
                        if (Optional.ofNullable(bidderFileAmount).isPresent()) {
                            biddingDto.setBidderFileAmount(Float.parseFloat(bidderFileAmount.replaceAll("元", "")));
                        } else {
                            biddingDto.setBidderFileAmount(0f);
                        }
                    }
                    if (Pattern.matches(".{2}投标文件的递交$", s2)) {
                        break;
                    } else {
                        sb.append(s2).append("\n");
                    }
                }
                String bidderDocInfo = sb.toString();
                biddingDto.setBidderDocInfo(bidderDocInfo);

                //招标文件获取时间
                Matcher matcher = null;
                ArrayList<String> list = new ArrayList<>();
                if (s1.contains("招标文件出售时间")) {
                    matcher = DATE_COMPILE.matcher(s1);
                    while (matcher.find()) {
                        list.add(matcher.group(0));
                    }
                    if (list.size() == 2) {
                        getBidDocStartAt = list.get(0).replaceAll("[年月]", "-").replaceAll("日", "");
                        getBidDocEndAt = list.get(1).replaceAll("[年月]", "-").replaceAll("日", "");
                        biddingDto.setGetBidDocStartAt(DateUtils.dateTime(DateUtils.YYYY_MM_DD, getBidDocStartAt));
                        biddingDto.setGetBidDocEndAt(DateUtils.dateTime(DateUtils.YYYY_MM_DD, getBidDocEndAt));
                    }
                } else {
                    String s2 = split[i + 1];
                    if (s2.contains("年") && s2.contains("月") && s2.contains("日")) {
                        matcher = DATE_COMPILE.matcher(s2);
                        while (matcher.find()) {
                            list.add(matcher.group(0));
                        }
                        if (list.size() == 2) {
                            getBidDocStartAt = list.get(0).replaceAll("[年月]", "-").replaceAll("日", "");
                            getBidDocEndAt = list.get(1).replaceAll("[年月]", "-").replaceAll("日", "");
                            biddingDto.setGetBidDocStartAt(DateUtils.dateTime(DateUtils.YYYY_MM_DD, getBidDocStartAt));
                            biddingDto.setGetBidDocEndAt(DateUtils.dateTime(DateUtils.YYYY_MM_DD, getBidDocEndAt));
                        }
                    } else {
                        matcher = YYYY_MM_DD.matcher(s2);
                        while (matcher.find()) {
                            list.add(matcher.group(0));
                        }
                        if (list.size() == 2) {
                            getBidDocStartAt = list.get(0);
                            getBidDocEndAt = list.get(1);
                            biddingDto.setGetBidDocStartAt(DateUtils.dateTime(DateUtils.YYYY_MM_DD, getBidDocStartAt));
                            biddingDto.setGetBidDocEndAt(DateUtils.dateTime(DateUtils.YYYY_MM_DD, getBidDocEndAt));
                        }
                    }
                }
            }

            //投标文件的递交
            if (Pattern.matches(".{2}投标文件的递交$", s1)) {
                StringBuffer sb = new StringBuffer();
                Matcher matcher = null;
                for (int j = i; j < split.length; j++) {
                    String s2 = split[j];

                    //投标保证金
                    if (s2.contains("保证金") && s2.contains("元")) {
                        if (s2.contains("万元")) {
                            matcher = WAN_YUAN_COMPILE.matcher(s2);
                            while (matcher.find()) {
                                guaranteeAmount = matcher.group(0);
                            }
                            if (Optional.ofNullable(guaranteeAmount).isPresent()) {
                                biddingDto.setGuaranteeAmount(Long.parseLong(guaranteeAmount.replaceAll("万元", "")) * 10000);
                            } else {
                                matcher = YUAN_COMPILE.matcher(s2);
                                while (matcher.find()) {
                                    guaranteeAmount = matcher.group(0);
                                }
                                if (Optional.ofNullable(guaranteeAmount).isPresent()) {
                                    biddingDto.setGuaranteeAmount(Long.parseLong(guaranteeAmount.replaceAll("元", "")));
                                } else {
                                    biddingDto.setGuaranteeAmount(0l);
                                }
                            }
                        } else {
                            matcher = YUAN_COMPILE.matcher(s2);
                            while (matcher.find()) {
                                guaranteeAmount = matcher.group(0);
                            }
                            if (Optional.ofNullable(guaranteeAmount).isPresent()) {
                                biddingDto.setGuaranteeAmount(Long.parseLong(guaranteeAmount.replaceAll("元", "")));
                            } else {
                                biddingDto.setGuaranteeAmount(0l);
                            }
                        }
                    }
                    if (Pattern.matches(".{2}开标$", s2) || Pattern.matches(".{2}发布公告的媒介$", s2)) {
                        break;
                    } else {
                        sb.append(s2).append("\n");
                    }
                }
                String postBidDocInfo = sb.toString();
                biddingDto.setPostBidDocInfo(postBidDocInfo);
            }

            //发布公告的媒介
            if (Pattern.matches(".{2}发布公告的媒介$", s1)) {
                StringBuffer sb = new StringBuffer();
                for (int j = i; j < split.length; j++) {
                    String s2 = split[j];
                    if (Pattern.matches(".{2}开标$", s2) || Pattern.matches(".{2}联系方式$", s2)) {
                        break;
                    } else {
                        sb.append(s2).append("\n");
                    }
                }
                String publishMedia = sb.toString();
                biddingDto.setPublishMedia(publishMedia);
            }

            //联系方式
            if (Pattern.matches(".{2}联系方式$", s1)) {
                StringBuffer sb = new StringBuffer();
                for (int j = i; j < split.length; j++) {
                    String s2 = split[j];
                    sb.append(s2).append("\n");
                }
                String linkInfo = sb.toString();
                biddingDto.setLinkInfo(linkInfo);
            }

            //开标时间
            if (Pattern.matches(".{2}开标$", s1)) {
                String s2 = split[i + 1];
                Matcher matcher = YYYY_MM_DD_HH_MM.matcher(s2);
                String s3 = "";
                while (matcher.find()) {
                    s3 = matcher.group(0);
                }
                if (!s3.isEmpty()) {
                    String year = s3.substring(0, s3.indexOf("年"));
                    String month = s3.substring(s3.indexOf("年") + 1, s3.indexOf("月"));
                    String day = s3.substring(s3.indexOf("月") + 1, s3.indexOf("日"));
                    String hour = s3.substring(s3.indexOf("日") + 1, s3.indexOf("时"));
                    String second = s3.substring(s3.indexOf("时") + 1, s3.indexOf("分"));
                    bidOpenAt = year + "-" + month + "-" + day + " " + hour + ":" + second + ":00";
                    biddingDto.setBidOpenAt(DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS, bidOpenAt));
                }
            }
        }
    }


    /**
     * 附件处理
     *
     * @param attachmentList
     * @param bidId
     * @param attachDtoMapper
     * @param biddingAttachDtoMapper
     */
    private void attachProcess(List<JSONObject> attachmentList, String bidId, BizAttachDtoMapper attachDtoMapper, BidBiddingAttachDtoMapper biddingAttachDtoMapper) {
        if (attachmentList != null && attachmentList.size() > 0) {
            for (JSONObject jsonObject : attachmentList) {
                HashMap<String, String> attachMap = jsonObjToMap(jsonObject);
                String attachmentid = attachMap.get("ATTACHMENTID");
                String name = attachMap.get("NAME");
                HashMap<String, String> hashMap = downLoadAttach(attachmentid, name);
                String type = name.substring(name.indexOf("."));
                //存入附件表
                BizAttachDto attachDto = new BizAttachDto();
                String attachId = StringUtils.getUuid32();
                attachDto.setAttachId(attachId);
                attachDto.setFileSize(Double.valueOf(hashMap.get("size")));
                attachDto.setFileName(name);
                attachDto.setFileType(type);
                attachDto.setFileUri(hashMap.get("path"));
                attachDto.setCreateTime(new Date());
                attachDto.setOwnerId(bidId);
                attachDto.setOwnerType(FileOwnerTypeEnum.BIDDING.getKey());
                attachDtoMapper.insert(attachDto);

                //存入bid中间表
                BidBiddingAttachDto biddingAttachDto = new BidBiddingAttachDto();
                biddingAttachDto.setId(StringUtils.getUuid32());
                biddingAttachDto.setAttachId(attachId);
                biddingAttachDto.setBiddingId(bidId);
                biddingAttachDtoMapper.insert(biddingAttachDto);
            }
        }
    }

    /**
     * JSONObject转Map
     *
     * @param jsonObject
     * @return
     */
    private HashMap<String, String> jsonObjToMap(JSONObject jsonObject) {
        HashMap<String, String> hashMap = new HashMap<>(16);
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            hashMap.put(entry.getKey(), (String) entry.getValue());
        }
        return hashMap;
    }
}
