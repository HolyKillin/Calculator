package shizhan.killin.com.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{
    Button  bt_AC,bt_Back,bt_Percent,bt_Divide,
            bt_Num7,bt_Num8,bt_Num9,bt_Multiply,
            bt_Num4,bt_Num5,bt_Num6,bt_Subtract,
            bt_Num1,bt_Num2,bt_Num3,bt_Plus,
            bt_Point,bt_Num0,bt_Equal;
    private TextView tv_Result;

    private String existedText;

    private boolean isCounted = false;

    private boolean startWithOperator = false;

    private boolean startWithSubtract = false;

    private boolean noStartWithOperator = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Integer[] ids = new Integer[]{R.id.bt_AC,R.id.bt_back,R.id.bt_percent,R.id.bt_divide,
                R.id.bt_num7,R.id.bt_num8,R.id.bt_num9,R.id.bt_multiply,
                R.id.bt_num4,R.id.bt_num5,R.id.bt_num6,R.id.bt_subtract,
                R.id.bt_num1,R.id.bt_num2,R.id.bt_num3,R.id.bt_plus,
                R.id.bt_point,R.id.bt_zero,R.id.bt_equal};
        Button[] buttons = new Button[]{bt_AC,bt_Back,bt_Percent,bt_Divide,
                bt_Num7,bt_Num8,bt_Num9,bt_Multiply,
                bt_Num4,bt_Num5,bt_Num6,bt_Subtract,
                bt_Num1,bt_Num2,bt_Num3,bt_Plus,
                bt_Point,bt_Num0,bt_Equal};

        for (int i = 0; i<=buttons.length-1;i++){
            buttons[i] = findViewById(ids[i]);
            buttons[i].setOnClickListener(this);
        }


        tv_Result = findViewById(R.id.tv_result);

        existedText = tv_Result.getText().toString();


    }



    /**
     * 进行运算，得到结果
     * @return  返回结果
     */
    private String getResult() {

        /**
         * 结果
         */
        String tempResult = null;
        /**
         * 两个String类型的参数
         */
        String param1 = null;
        String param2 = null;
        /**
         * 转换后的两个double类型的参数
         */
        double arg1 = 0;
        double arg2 = 0;
        double result = 0;

        getCondition();

        /**
         * 如果有运算符，则进行运算
         * 没有运算符，则把已经存在的数据再传出去
         */
        if ( startWithOperator || noStartWithOperator || startWithSubtract) {

            if (existedText.contains("+")) {
                /**
                 * 先获取两个参数
                 */
                param1 = existedText.substring(0, existedText.indexOf("+"));
                param2 = existedText.substring(existedText.indexOf("+") + 1);
                /**
                 * 如果第二个参数为空，则还是显示当前字符
                 */
                if (param2.equals("")){
                    tempResult = existedText;
                } else {
                    /**
                     * 转换String为Double
                     * 计算后再转换成String类型
                     * 进行正则表达式处理
                     */
                    arg1 = Double.parseDouble(param1);
                    arg2 = Double.parseDouble(param2);
                    result = arg1 + arg2;
                    tempResult = String.format("%f", result);
                    tempResult = subZeroAndDot(tempResult);
                }


            } else if (existedText.contains("×")) {

                param1 = existedText.substring(0, existedText.indexOf("×"));
                param2 = existedText.substring(existedText.indexOf("×") + 1);

                if (param2.equals("")){
                    tempResult = existedText;
                } else {
                    arg1 = Double.parseDouble(param1);
                    arg2 = Double.parseDouble(param2);
                    result = arg1 * arg2;
                    tempResult = String.format("%f", result);
                    tempResult = subZeroAndDot(tempResult);
                }

            } else if (existedText.contains("÷")) {

                param1 = existedText.substring(0, existedText.indexOf("÷"));
                param2 = existedText.substring(existedText.indexOf("÷") + 1);

                if (param2.equals("0")){
                    tempResult = "error";
                } else if (param2.equals("")){
                    tempResult = existedText;
                } else {
                    arg1 = Double.parseDouble(param1);
                    arg2 = Double.parseDouble(param2);
                    result = arg1 / arg2;
                    tempResult = String.format("%f", result);
                    tempResult = subZeroAndDot(tempResult);
                }

            } else if (existedText.contains("-")) {

                /**
                 * 这里是以最后一个 - 号为分隔去取出两个参数
                 * 进到这个方法，必须满足有运算公式
                 * 而又避免了第一个参数是负数的情况
                 */
                param1 = existedText.substring(0, existedText.lastIndexOf("-"));
                param2 = existedText.substring(existedText.lastIndexOf("-") + 1);

                if (param2.equals("")){
                    tempResult = existedText;
                } else {
                    arg1 = Double.parseDouble(param1);
                    arg2 = Double.parseDouble(param2);
                    result = arg1 - arg2;
                    tempResult = String.format("%f", result);
                    tempResult = subZeroAndDot(tempResult);
                }

            }
            /**
             * 如果数据长度大于等于10位，进行科学计数
             *
             * 如果有小数点，再判断小数点前是否有十个数字，有则进行科学计数
             */
            if (tempResult.length() >= 10) {
                tempResult = String.format("%e", Double.parseDouble(tempResult));
            } else if (tempResult.contains(".")) {
                if (tempResult.substring(0, tempResult.indexOf(".")).length() >= 10) {
                    tempResult = String.format("%e", Double.parseDouble(tempResult));
                }
            }
        } else {
            tempResult = existedText;
        }

        return tempResult;
    }
    /**
     * 先判断是否按过等于号
     * 是 按数字则显示当前数字
     * 否 在已有的表达式后添加字符
     *
     * 判断数字是否就是一个 0
     * 是 把字符串设置为空字符串。
     *   1、打开界面没有运算过的时候，AC键归零或删除完归零的时候，会显示一个 0
     *   2、当数字是 0 的时候，设置成空字符串，再按 0 ，数字会还是 0，不然可以按出 000 这种数字
     * 否 添加按下的键的字符
     *
     * 判断数字是否包含小数点
     * 是 数字不能超过十位
     * 否 数字不能超过九位
     *
     * 进行上面的判断后，再判断数字是否超过长度限制
     * 超过不做任何操作
     * 没超过可以再添数字
     */
    private String isOverRange(String existedText, String s) {
        /**
         * 判断是否计算过
         */
        if (!isCounted){
            /**
             * 判断是否是科学计数
             * 是 文本置零
             */
            if (existedText.contains("e")){
                existedText = "0";
            }
            /**
             * 判断是否只有一个 0
             * 是 文本清空
             */
            if (existedText.equals("0")){
                existedText = "";
            }
            /**
             * 判断是否有运算符
             * 是 判断第二个数字
             * 否 判断整个字符串
             */
            if (existedText.contains("+") || existedText.contains("-") ||
                    existedText.contains("×") || existedText.contains("÷")){
                /**
                 * 包括运算符时 两个数字 判断第二个数字
                 * 两个参数
                 */
                String param2 = null;
                if (existedText.contains("+")){
                    param2 = existedText.substring(existedText.indexOf("+")+1);
                } else if (existedText.contains("-")){
                    param2 = existedText.substring(existedText.indexOf("-")+1);
                } else if (existedText.contains("×")){
                    param2 = existedText.substring(existedText.indexOf("×")+1);
                } else if (existedText.contains("÷")){
                    param2 = existedText.substring(existedText.indexOf("÷")+1);
                }

                //            Log.d("Anonymous param1",param1);
                //            Log.d("Anonymous param2",param2);
                if (existedText.substring(existedText.length()-1).equals("+") ||
                        existedText.substring(existedText.length()-1).equals("-") ||
                        existedText.substring(existedText.length()-1).equals("×") ||
                        existedText.substring(existedText.length()-1).equals("÷")){
                    existedText += s;
                } else {
                    if (param2.contains(".")){
                        if (param2.length() >= 10){

                        } else {
                            existedText += s;
                        }
                    } else {
                        if (param2.length() >= 9){

                        } else {
                            existedText += s;
                        }
                    }
                }
            } else {
                /**
                 * 不包括运算符时 一个数字
                 */
                if (existedText.contains(".")){
                    if (existedText.length() >= 10){

                    } else {
                        existedText += s;
                    }
                } else {
                    if (existedText.length() >= 9){

                    } else {
                        existedText += s;
                    }
                }
            }

            isCounted = false;

        } else {

            existedText = s;
            isCounted = false;

        }


        return existedText;
    }


    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s 传入的字符串
     * @return 修改之后的字符串
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 判断表达式
     *
     * 为了按等号是否进行运算
     * 以及出现两个运算符（第一个参数如果为负数的符号不计）先进行运算再添加运算符
     */
    private boolean judgeExpression() {

        getCondition();

        String tempParam2 = null;

        if ( startWithOperator || noStartWithOperator || startWithSubtract) {

            if (existedText.contains("+")) {
                /**
                 * 先获取第二个参数
                 */
                tempParam2 = existedText.substring(existedText.indexOf("+") + 1);
                /**
                 * 如果第二个参数为空，表达式不成立
                 */
                if (tempParam2.equals("")) {
                    return false;
                } else {
                    return true;
                }
            } else if (existedText.contains("×")) {

                tempParam2 = existedText.substring(existedText.indexOf("×") + 1);

                if (tempParam2.equals("")) {
                    return false;
                } else {
                    return true;
                }

            } else if (existedText.contains("÷")) {

                tempParam2 = existedText.substring(existedText.indexOf("÷") + 1);

                if (tempParam2.equals("")) {
                    return false;
                } else {
                    return true;
                }

            } else if (existedText.contains("-")) {

                /**
                 * 这里是以最后一个 - 号为分隔去取出两个参数
                 * 进到这个方法，必须满足有运算公式
                 * 而又避免了第一个参数是负数的情况
                 */
                tempParam2 = existedText.substring(existedText.lastIndexOf("-") + 1);

                if (tempParam2.equals("")) {
                    return false;
                } else {
                    return true;
                }

            }
        }
        return false;
    }

    /**
     * 取得判断条件
     */
    private void getCondition() {
        /**
         * 以负号开头，且运算符不是是减号
         * 例如：-21×2
         */
        startWithOperator = existedText.startsWith("-") && ( existedText.contains("+") ||
                existedText.contains("×") || existedText.contains("÷") );
        /**
         * 以负号开头，且运算符是减号
         * 例如：-21-2
         */
        startWithSubtract = existedText.startsWith("-") && ( existedText.lastIndexOf("-") != 0 );
        /**
         * 不以负号开头，且包含运算符
         * 例如：21-2
         */
        noStartWithOperator = !existedText.startsWith("-") && ( existedText.contains("+") ||
                existedText.contains("-") || existedText.contains("×") || existedText.contains("÷"));
    }

    @Override
    public void onClick(View v) {
                switch (v.getId()){
                    /**
                     * 数字
                     */
                    case R.id.bt_zero:
                        existedText = isOverRange(existedText,"0");
                        break;
                    case R.id.bt_num1:
                        existedText = isOverRange(existedText,"1");
                        break;
                    case R.id.bt_num2:
                        existedText = isOverRange(existedText,"2");
                        break;
                    case R.id.bt_num3:
                        existedText = isOverRange(existedText,"3");
                        break;
                    case R.id.bt_num4:
                        existedText = isOverRange(existedText,"4");
                        break;
                    case R.id.bt_num5:
                        existedText = isOverRange(existedText,"5");
                        break;
                    case R.id.bt_num6:
                        existedText = isOverRange(existedText,"6");
                        break;
                    case R.id.bt_num7:
                        existedText = isOverRange(existedText,"7");
                        break;
                    case R.id.bt_num8:
                        existedText = isOverRange(existedText,"8");
                        break;
                    case R.id.bt_num9:
                        existedText = isOverRange(existedText,"9");
                        break;
                    /**
                     * 运算符
                     */
                    case R.id.bt_plus:
                        /**
                         * 判断已有的字符是否是科学计数
                         * 是 置零
                         * 否 进行下一步
                         *
                         * 判断表达式是否可以进行计算
                         * 是 先计算再添加字符
                         * 否 添加字符
                         *
                         * 判断计算后的字符是否是 error
                         * 是 置零
                         * 否 添加运算符
                         */
                        if (!existedText.contains("e")) {

                            if (judgeExpression()) {
                                existedText = getResult();
                                if (existedText.equals("error")){

                                } else {
                                    existedText += "+";
                                }
                            } else {

                                if (isCounted) {
                                    isCounted = false;
                                }

                                if ((existedText.substring(existedText.length() - 1)).equals("-")) {
                                    existedText = existedText.replace("-", "+");
                                } else if ((existedText.substring(existedText.length() - 1)).equals("×")) {
                                    existedText = existedText.replace("×", "+");
                                } else if ((existedText.substring(existedText.length() - 1)).equals("÷")) {
                                    existedText = existedText.replace("÷", "+");
                                } else if (!(existedText.substring(existedText.length() - 1)).equals("+")) {
                                    existedText += "+";
                                }
                            }
                        } else {
                            existedText = "0";
                        }

                        break;
                    case R.id.bt_subtract:

                        if (!existedText.contains("e")) {
                            if (judgeExpression()) {
                                existedText = getResult();
                                if (existedText.equals("error")){

                                } else {
                                    existedText += "-";
                                }
                            } else {

                                if (isCounted) {
                                    isCounted = false;
                                }

                                if ((existedText.substring(existedText.length() - 1)).equals("+")) {
                                    //                    Log.d("Anonymous", "onClick: " + "进入减法方法");
                                    existedText = existedText.replace("+", "-");
                                } else if ((existedText.substring(existedText.length() - 1)).equals("×")) {
                                    existedText = existedText.replace("×", "-");
                                } else if ((existedText.substring(existedText.length() - 1)).equals("÷")) {
                                    existedText = existedText.replace("÷", "-");
                                } else if (!(existedText.substring(existedText.length() - 1)).equals("-")) {
                                    existedText += "-";
                                }
                            }
                        } else {
                            existedText = "0";
                        }
                        break;
                    case R.id.bt_multiply:

                        if (!existedText.contains("e")) {
                            if (judgeExpression()) {
                                existedText = getResult();
                                if (existedText.equals("error")){

                                } else {
                                    existedText += "×";
                                }

                            } else {

                                if (isCounted) {
                                    isCounted = false;
                                }

                                if ((existedText.substring(existedText.length() - 1)).equals("+")) {
                                    existedText = existedText.replace("+", "×");
                                } else if ((existedText.substring(existedText.length() - 1)).equals("-")) {
                                    existedText = existedText.replace("-", "×");
                                } else if ((existedText.substring(existedText.length() - 1)).equals("÷")) {
                                    existedText = existedText.replace("÷", "×");
                                } else if (!(existedText.substring(existedText.length() - 1)).equals("×")) {
                                    existedText += "×";
                                }
                            }
                        } else {
                            existedText = "0";
                        }
                        break;
                    case R.id.bt_divide:

                        if (!existedText.contains("e")) {
                            if (judgeExpression()) {
                                existedText = getResult();
                                if (existedText.equals("error")){

                                } else {
                                    existedText += "÷";
                                }

                            } else {

                                if (isCounted) {
                                    isCounted = false;
                                }

                                if ((existedText.substring(existedText.length() - 1)).equals("+")) {
                                    existedText = existedText.replace("+", "÷");
                                } else if ((existedText.substring(existedText.length() - 1)).equals("-")) {
                                    existedText = existedText.replace("-", "÷");
                                } else if ((existedText.substring(existedText.length() - 1)).equals("×")) {
                                    existedText = existedText.replace("×", "÷");
                                } else if (!(existedText.substring(existedText.length() - 1)).equals("÷")) {
                                    existedText += "÷";
                                }
                            }
                        } else {
                            existedText = "0";
                        }
                        break;
                    case R.id.bt_equal:
                        existedText = getResult();
                        isCounted = true;
                        break;
                    /**
                     * 其他
                     */
                    case R.id.bt_point:
                        /**
                         * 判断是否运算过
                         * 否
                         *   判断是否有运算符，有 判断运算符之后的数字，无 判断整个数字
                         *   判断数字是否过长，是则不能添加小数点，否则可以添加
                         *   判断已经存在的数字里是否有小数点
                         * 是
                         *   字符串置为 0.
                         */
                        if (!isCounted){

                            if (existedText.contains("+") || existedText.contains("-") ||
                                    existedText.contains("×") || existedText.contains("÷") ){

                                String param1 = null;
                                String param2 = null;

                                if (existedText.contains("+")) {
                                    param1 = existedText.substring(0, existedText.indexOf("+"));
                                    param2 = existedText.substring(existedText.indexOf("+") + 1);
                                } else if (existedText.contains("-")) {
                                    param1 = existedText.substring(0, existedText.indexOf("-"));
                                    param2 = existedText.substring(existedText.indexOf("-") + 1);
                                } else if (existedText.contains("×")) {
                                    param1 = existedText.substring(0, existedText.indexOf("×"));
                                    param2 = existedText.substring(existedText.indexOf("×") + 1);
                                } else if (existedText.contains("÷")) {
                                    param1 = existedText.substring(0, existedText.indexOf("÷"));
                                    param2 = existedText.substring(existedText.indexOf("÷") + 1);
                                }
                                Log.d("Anonymous param1",param1);
                                Log.d("Anonymous param2",param2);

                                boolean isContainedDot = param2.contains(".");
                                if (param2.length() >= 9){

                                } else if (!isContainedDot){
                                    if (param2.equals("")){
                                        existedText += "0.";
                                    } else {
                                        existedText += ".";
                                    }
                                } else {
                                    return;
                                }
                            } else {
                                boolean isContainedDot = existedText.contains(".");
                                if (existedText.length() >= 9){

                                } else if (!isContainedDot){
                                    existedText += ".";
                                } else {
                                    return;
                                }
                            }
                            isCounted = false;

                        } else {
                            existedText = "0.";
                            isCounted = false;
                        }


                        break;
                    case R.id.bt_percent:
                        /**
                         * 判断数字是否有运算符
                         * 是 不做任何操作
                         * 否 进行下一步
                         *
                         * 判断数字是否是 0
                         * 是 不做任何操作
                         * 否 进行除百
                         *
                         * 将字符串转换成double类型，进行运算后，再转换成String型
                         */
                        if (existedText.equals("error")){

                        } else {

                            getCondition();

                            if (startWithOperator || startWithSubtract || noStartWithOperator) {

                            } else {
                                if (existedText.equals("0")) {
                                    return;
                                } else {
                                    double temp = Double.parseDouble(existedText);
                                    temp = temp / 100;
                                    existedText = String.valueOf(temp);
                                }
                            }
                        }
                        break;
                    case R.id.bt_back:
                        /**
                         * 字符串长度大于 0 时才截取字符串
                         * 如果长度为 1，则直接把字符串设置为 0
                         */
                        if (existedText.equals("error")){
                            existedText = "0";
                        } else if (existedText.length() > 0){
                            if (existedText.length() == 1) {
                                existedText = "0";
                            } else {
                                existedText = existedText.substring(0,existedText.length()-1);
                            }
                        }
                        break;
                    case R.id.bt_AC:
                        existedText = "0";
                        break;
                }
                /**
                 * 设置显示
                 */
                tv_Result.setText(existedText);
    }
}

