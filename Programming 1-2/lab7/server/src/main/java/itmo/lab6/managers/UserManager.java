package itmo.lab6.managers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * Менеджер пользователей: регистрация и аутентификация.
 * Пароли хранятся как SHA-224 хеш в hex-строке.
 */
public class UserManager {
    private static final Logger logger = Logger.getLogger(UserManager.class.getName());
    private static final String HASH_ALG = "SHA-224";

    private final UserDao userDao;

    public UserManager(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Хеширует пароль алгоритмом SHA-224.
     * @return hex-строка (56 символов)
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALG);
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-224 не поддерживается", e);
        }
    }

    /**
     * Регистрирует нового пользователя.
     * @return true если успешно, false если логин занят
     */
    public boolean register(String login, String password) {
        if (login == null || login.isBlank()) return false;
        if (password == null || password.isBlank()) return false;
        String hash = hashPassword(password);
        boolean result = userDao.insert(login, hash);
        if (result) logger.info("Зарегистрирован пользователь: " + login);
        else logger.warning("Логин уже занят или ошибка регистрации: " + login);
        return result;
    }

    /**
     * Проверяет логин/пароль.
     * @return true если данные верны
     */
    public boolean authenticate(String login, String password) {
        if (login == null || login.isBlank()) return false;
        if (password == null || password.isBlank()) return false;
        String hash = hashPassword(password);
        return userDao.authenticate(login, hash);
    }
}
