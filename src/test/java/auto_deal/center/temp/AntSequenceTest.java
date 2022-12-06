package auto_deal.center.temp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AntSequenceTest {

    @Test
    @DisplayName( "개미수열 테스트한다.")
    void getAntSequence(){
        int nums = 4;

        String start = "11";
        String result = "11";
        int cnt = 0;


        for (int i = 0; i < nums; i++) {

            start = result;
            String temp = String.valueOf(start.charAt(0));
            result = "";

            for (int j = 0; j < start.length(); j++) {

                if( String.valueOf(start.charAt(j)).equals(temp) ){
                    cnt++;
                }else{
                    result = result + temp + String.valueOf(cnt);
                    temp = String.valueOf(start.charAt(j));
                    cnt = 1;
                }
            }

            if( cnt >= 1){
                result = result + temp + String.valueOf(cnt);
                cnt = 0;
            }
        }

        if( nums == 1){
            result = "1";
        }

    }

}
