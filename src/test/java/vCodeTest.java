import com.auction_sys.util.FTPUtil;
import com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;
/**
 * Created By liuda on ${date}
 */
public class vCodeTest {
    /** 生成动态图片验证码 * */

    public static void main(String[] args) throws IOException {
        //创建图像
        int width=100;
        int hight=50;
        BufferedImage image = new BufferedImage(width,hight,BufferedImage.TYPE_INT_RGB);
        //创建图层（画板）
        Graphics g = image.getGraphics();
        //确定画笔颜色
        g.setColor(Color.ORANGE);
        //填充一个矩形
        g.fillRect(0, 0, width, hight);
        //在打矩形中画一个小矩形，放验证码，得到边框效果，可以不要。
        g.setColor(Color.WHITE);
        g.fillRect(1, 1, width - 2, hight - 2);
        //填充字符
        String data = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        //将生成的数据放在容器中
        StringBuffer buff = new StringBuffer();
        //随机获取四个字符
        Random random = new Random();
        for(int i=0;i<4;i++){
        //从62个字符里随机获取字符
            int index = random.nextInt(62);
        //截取一个字符
            String str = data.substring(index, index+1);
        //设置字体颜色
            if(i==0){
                g.setColor(Color.RED);
            }else if(i==1){
                g.setColor(Color.BLUE);
            }else if(i==2){
                g.setColor(Color.GREEN);
            }else if(i==3){
                g.setColor(Color.black);
            }

            //设置字体样式、格式、大小
            String[] sty = {"楷书","隶书","仿宋","Courier New"};
            int col = Font.BOLD;
            int size = 30;
            int len = random.nextInt(sty.length);
            Font f = new Font(sty[len],col,size);
            g.setFont(f);
        /**
         * 把字符写到图片中取,
         * 注意：字体格式、大小、颜色等设置必须放在字体写到图片之前，否则，第一个将无效。
         */
            g.drawString(str, 25*i, 35);
            //将生成的数据放在容器中 已校验验证码是否正确
            buff.append(str);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image,"JPEG",os);
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
//            FTPUtil.uploadFile(is);

        }

    }

}
