package com.example.boot1907.assist;

import com.example.boot1907.pojo.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

/**
 * @author bigpig
 * @version 1.0
 * @date 18-5-24
 */
@Component
public class PasswordHelper {
	// 生成随机长度字节，用于生成盐
    private final RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    // 散列算法名称，这里使用md5
    private final String algorithmName = "md5";
    // 散列复杂度次数
    private final int hashIterations = 2;

    public void encryptPassword(User user) {
        user.setSalt(randomNumberGenerator.nextBytes().toHex());
        String newPassword = new SimpleHash(
                algorithmName,
                user.getPasswd(),
                ByteSource.Util.bytes(user.getSalt()),
                hashIterations).toHex();

        user.setPasswd(newPassword);
    }
}
