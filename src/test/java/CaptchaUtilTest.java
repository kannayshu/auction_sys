import com.auction_sys.util.CaptchaUtil;
import com.auction_sys.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created By liuda on 2018/5/24
 */
@Slf4j
public class CaptchaUtilTest {
    public static void main(String[] args) throws IOException {
        for (int i=0;i<100;i++){
            CaptchaUtil captchaUtil = CaptchaUtil.Instance();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(captchaUtil.getImage(),"JPEG",os);
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            System.out.println(captchaUtil.getString());
        }

    }
}
