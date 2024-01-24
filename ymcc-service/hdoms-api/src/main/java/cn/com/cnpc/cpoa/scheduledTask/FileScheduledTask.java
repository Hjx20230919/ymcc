package cn.com.cnpc.cpoa.scheduledTask;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.common.json.JSON;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.service.AttachService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/30 12:21
 * @Description:定时删除临时文件
 */
@Slf4j
@Component
public class FileScheduledTask {

    @Autowired
    AttachService attachService;

    @Value("${file.tempurl}")
    private  String TEMPURL;

    /**
     * 每日00:00删除临时目录的文件
     */
    @Log(logContent = "每日00:00删除临时目录的文件",logModule = LogModule.FILE,logType = LogType.OPERATION)
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduled() throws Exception{
        Map<String,Object> params=new HashMap<>();
        params.put("fileUri",TEMPURL);
        List<BizAttachDto> bizAttachDtos = attachService.selectList(params);
        //删除临时文件
        for (BizAttachDto attachDto:bizAttachDtos) {
            attachService.deleteById(attachDto.getAttachId(),"");
            log.info("=====>>>>>删除临时文件{}", JSON.marshal(attachDto));
        }
        //删除TEMPURL目录下所有目录
        deleteDir(new File(TEMPURL));
        //新建空TEMPURL目录
        File dest = new File(TEMPURL);
        // 检测是否存在目录
        if (!dest.exists()) {
            dest.mkdirs();// 新建文件夹
        }
    }
    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }


    /**
     *测试
     */
    public static void main(String[] args) {
        String newDir2 ="D:/temp/";
        boolean success = deleteDir(new File(newDir2));
        if (success) {
            System.out.println("Successfully deleted populated directory: " + newDir2);
        } else {
            System.out.println("Failed to delete populated directory: " + newDir2);
        }
        File dest = new File(newDir2);
        // 检测是否存在目录
        if (!dest.exists()) {
            dest.mkdirs();// 新建文件夹
        }
    }
}
