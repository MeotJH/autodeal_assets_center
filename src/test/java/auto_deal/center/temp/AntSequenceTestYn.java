package auto_deal.center.temp;

import java.util.ArrayList;

public class AntSequenceTestYn {

    public static void main(String[] args) {

        //초기 값 찍고 시작
        String strNumber = "1"; //숫자 저장
        System.out.println(strNumber);//처음 "1" 출력

        int loopCnt = 5;       //개미수열 반복행수 지정

        for (int i = 0; i <= loopCnt; i++) {
            ArrayList<String> result = LoopAntSequence(strNumber);
            printAntSequence(result);
            strNumber = getNextStrNum(result);
        }

    }

    private static void printAntSequence(ArrayList<String> result) {
        for (String s : result) {
            System.out.print(s);
        }
        System.out.println(" ");
    }

    //개미수열 순회 반복
    public static ArrayList<String> LoopAntSequence(String strNumber) {
        ArrayList<String> result = new ArrayList<>();
        char[] antNumArr = strNumber.toCharArray();

        int count = 0; //반복되는 숫자의 갯수를 카운트
        char targetNum = strNumber.toCharArray()[0];
        char nextNum = ' ';

        for (int i = 0; i < antNumArr.length; i++) {

            char currNum = antNumArr[i];

            // 다음숫자를 구한다.
            if (i != antNumArr.length - 1) {
                nextNum = antNumArr[i + 1];
            } else {
                nextNum = ' ';
            }

            // 현재 Target숫자와 배열에서 꺼낸 숫자가 갔다면, 카운트를 센다.
            if (targetNum == currNum) {
                count = count + 1;
            }

            // 다음 숫자가 현재 숫자와 다르게 바뀐다면
            if (targetNum != nextNum) {
                //데이터 저장
                result.add(String.valueOf(targetNum));
                result.add(String.valueOf(count));

                //초기화
                targetNum = nextNum;
                count = 0;
            }
        }
        return result;
    }

    public static String getNextStrNum(ArrayList<String> result) {
        StringBuilder nextStr = new StringBuilder();
        //다음행 숫자구하기
        for (String s : result) {
            nextStr.append(s);
        }
        return nextStr.toString();
    }
}
