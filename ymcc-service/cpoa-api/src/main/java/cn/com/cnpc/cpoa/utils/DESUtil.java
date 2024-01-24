package cn.com.cnpc.cpoa.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.util.Base64;

/**
 * @Auther: hukun
 * @Date: 2021/12/04/21:47
 * @Description:
 */
public class DESUtil {

    /**
     * 偏移变量，固定占8位字节
     */
    private final static String IV_PARAMETER = "12345678";
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";
    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    /**
     * 默认编码
     */
    private static final String CHARSET = "utf-8";

    /**
     * 生成key
     *
     * @param password
     * @return
     * @throws Exception
     */
    private static Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }


    /**
     * DES加密字符串
     *
     * @param password 加密密码，长度不能够小于8位
     * @param data 待加密字符串
     * @return 加密后内容
     */
    public static String encrypt(String data) {
        String password="12345678";
        if (password== null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null)
            return null;
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(data.getBytes(CHARSET));

            //JDK1.8及以上可直接使用Base64，JDK1.7及以下可以使用BASE64Encoder
            //Android平台可以使用android.util.Base64
            return new String(Base64.getEncoder().encode(bytes));

        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * DES解密字符串
     *
     * @param password 解密密码，长度不能够小于8位
     * @param data 待解密字符串
     * @return 解密后内容
     */
    public static String decrypt(String data) {
        String password="12345678";
        if (password== null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null)
            return null;
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes(CHARSET))), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }


    public static void main(String[] args) {
        String message="{" +
                "  \"touser\" : \"oHRZi5pZBRyoB1-aBJDXrh3TpO_U\"," +
                "  \"template_id\" : \"3ceyQzTh4P9iBv9ku_Vq97e8YTGnk8K54_0NxGHB4ds\"," +
                "  \"url\" : \"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb7095fc7e128db01&redirect_uri=http%3a%2f%2fwx.zhinglink.com%2fapp%2fhome%2fproj%2f758e43cbf6e947cebf5e79c5d8c9fc39&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect\"," +
                "  \"data\" : {" +
                "    \"first\" : {" +
                "      \"value\" : \"您有新的项目立项流程申请需要审批！\"," +
                "      \"color\" : \"#00000\"" +
                "    }," +
                "    \"keyword1\" : {" +
                "      \"value\" : \"立项-项目立项\"," +
                "      \"color\" : \"#00000\"" +
                "    }," +
                "    \"keyword2\" : {" +
                "      \"value\" : \"test-伊拉克\"," +
                "      \"color\" : \"#00000\"" +
                "    }," +
                "    \"keyword3\" : {" +
                "      \"value\" : \"2021年06月07日 17时43分06秒\"," +
                "      \"color\" : \"#000000\"" +
                "    }," +
                "    \"keyword4\" : null," +
                "    \"remark\" : {" +
                "      \"value\" : \"查看项目立项审批流程，点击下方详情进入！\"," +
                "      \"color\" : \"#0033CC\"" +
                "    }" +
                "  }" +
                "}";

        String encrypt = encrypt(message);
        String decrypt = decrypt("otSjOZnD5q3QEHpPrI7SDVv4UTPm30htGOc3zrSwnaGKThPzp05aozmFstNXpyAVDJlt1TYJAyA3GF0th1wh6eBX8kVx 7XM7\n" +
                "1lHfn Kr/5NPh6zQ5yDQjCspP9oGzwVn4fbaqGLlWKv03UWPZBAODMvD1MxcBTk6cH9QjPyE4xZC93D0HQXu Mhl6sCT3zBuXJiRaJlYyS927v7k5bBe5f6D/lPYzMCIz24yDso8VCnW1MzeOPmudXL6R4EuFVkrCI7dqFwlVfSNxwUcJIYV45sxdkZzMZ5cbdzCZP3p4/mjFuQKceTL3rQgjPZcAvhN30n25aHwRvy1CO67/r1dhi Rbou9aEZjPFkCCJHf/PUBOU2QnINZ6SYOiHFi2n9JNCcLfeQX70 3OPnxf NvtQN LILG eL1CH1DIv02RIE9EZ8031pfDc/x tkUUyMbEa1mrDyz4rRGMYoBDDwXGuj9T8cFwPLUaT2NeLEhJDQaOc50XgFwBiOa9 UQT4ynwS3NcTMFQI7yUg\n" +
                "boZ5rshDPeNUrMQhQQmDxqjK8XREo9xBckQTScRZGa8WLTg8Zepl2XHeD41yven3QshB9ZaAjpAlpPi75nM4JIxz6JORej7vrInegDIJPj3RLYHG1TmYMWlN7/rgjWSzg95VczE CK/EalEVmaoXvWC3qIb5slW3BHtyADiCb1HfUY7rvthFuKHF8oD3BkCbKwkXA9vntTYB3OW8o42SAuRnghZUPIy88lVSbe49bjlHzVVTjv5/cqc22a/m2lRgC6Dmn7llRzZZu1T 1Q1HM4gNhSIIkrNrGFGgxgtz2pW29N1 o3rp/AEZSqJWQ1eq gGnmWxJ9b9k8c 5GiZUy08WyWlToUjJM9wu/8o/x ehCkAqFtlTEhTMbQi9CzyHrCuxzn6zyEj8aB2iP0d3Nl1yNPGwUHdNGTenon/LoND2ES\n" +
                "vCPB3r0hG7lGbHcSzJJ3ibbSv59Jc3N69RrUiTMM6oZw47GvZV3JwwGLA7jgjcQGxA AY20cU8IIGtY54xpTxF0n4a3NKUo1lw7SFWuCqEKJQ6/pKL7F1wPYh3mIGiemGcrLZAg898VZCRcbJuAKsUZtXbc2bGtzPlqokpMy05MIPkPBY9KreN/3992UkNrW5oVXtrpyVK/TFy1H1QURSbY0wzhmoLNkY7GlcAfFtCte1qNrQqMWe5t os3pe9/KNZL4f/MUbhWeJsw1nSAxn1I5Z8NhYC3Wp5D 3VgDARwTKlrD9kH6Ab/y2OsKrTwA0KTzsP2 hyGlDNnTptQAwupa6sTTQBw4A44X5uhMdwOjEwrGBMvnYfrbHed3XhnhbAgDSVMY15iNn4zBar49q73ABNqWeikIaYKig3X3A6czUp\n" +
                "VuP7cxpII6ByWlyHDJ b/SBqPEElG7QmgIEDtNoT4M9O4wzVzWeY7QCZdo3c43BM=");
        System.out.println("encrypt"+decrypt);
        System.out.println("decrypt"+decrypt);
    }
}