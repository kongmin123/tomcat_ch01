package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloCopy4 {
 //源文件和目标文件
 final static String SOURCESTRING = "C://test";
 final static String TARGETSTRING = "C://testcopy";
 //final static String SOURCESTRING = "C://FIAS";
 //final static String TARGETSTRING = "C://FIASCOPY";
 //定义删除的开始部分和结束部分
 //final static String DELBEGIN = "读";
 //final static String DELBEGIN = "STORAGE";
 final static String DELBEGIN = "PCTFREE";
 //final static String DELEND = "的";
 final static String DELEND = "\\)"; 
 //定义替换的开始部分和结束部分，以及要替换的内容
 final static String REPBEGIN = "";
 final static String REPEND = "";
 final static String REPSTR = "";
 static StringBuffer sb = new StringBuffer("");
 
 public static void main(String[] args) throws FileNotFoundException, IOException{
  // TODO Auto-generated method stub
  System.out.println("HelloWorld");  
  if(!(new File(SOURCESTRING)).exists()){
       System.out.println("源文件" + SOURCESTRING + "不存在，无法复制！");
                return;
     }else{
      readfilefolder(DELBEGIN,DELEND,REPBEGIN,REPEND,REPSTR); 
      }  
 }
 
 //读取源文件夹中的所有文件和文件夹
 public static boolean readfilefolder(String delebegin,String deleend,String repbegin,String repend,String repstr) throws IOException {
  if((new File(SOURCESTRING)).isFile()){
         readfile(SOURCESTRING);
         writefile(TARGETSTRING,delebegin,deleend,repbegin,repend,repstr);
     }else if((new File(SOURCESTRING)).isDirectory()){
         readDirectory(SOURCESTRING,TARGETSTRING,delebegin,deleend,repbegin,repend,repstr);
    }
  return true;
 }
 
 //读取文件夹中的内容 
 private static void readDirectory(String sourcePathString,String targetPathString,String delebegin,String deleend,String repbegin,String repend,String repstr) throws IOException{           
    (new File(targetPathString)).mkdirs();
    System.out.println("开始读取文件夹" + sourcePathString +"中的内容");
    File[] files = new File(sourcePathString).listFiles();
    for(int i = 0; i < files.length; i++){
        if(files[i].isFile()){
            readfile(sourcePathString + File.separator + files[i].getName());
            writefile(targetPathString + File.separator + files[i].getName(),delebegin,deleend,repbegin,repend,repstr);
         }else if(files[i].isDirectory()){
            readDirectory(sourcePathString + File.separator + files[i].getName(),targetPathString + File.separator + files[i].getName(),delebegin,deleend,repbegin,repend,repstr);
         }    
   }
          System.out.println("读取文件夹" + sourcePathString + "成功");
   } 
 
   //读取某个文件的内容   
   public static void readfile(String absolutePath) throws IOException {
    System.out.println("开始读取文件" + absolutePath + "中的内容");
    if(sb.length()>0) {
     sb.setLength(0);
    }    
    //FileReader reader = new FileReader(absolutePath); 
       //BufferedReader br = new BufferedReader(reader);
       FileInputStream fis = new FileInputStream(absolutePath);
       InputStreamReader isr = new InputStreamReader(fis, "Shift_JIS"); //GBK替换成你文件内容使用的编码
       BufferedReader br = new BufferedReader(isr); 
       //str是按行读取的
       String str = null;     //   
       while((str = br.readLine()) != null) {
        /*if(!str.trim().equals("")) {     
         sb.append(str+System.getProperty("line.separator"));//用于换行
        } */ 
            byte[] bytes = str.getBytes(); 
               //String str = new String(bytes,"Shift-JIS");
               //String str2 = new String(bytes,"Shift-JIS");
         sb.append(str+System.getProperty("line.separator"));//用于换行         
       }
       //去除多余的一个换行
       /*sb.deleteCharAt(sb.length()-1);  
       sb.deleteCharAt(sb.length()-1); */ 
       br.close();
       //reader.close(); 
       isr.close();
       fis.close();
       //System.out.println("读取文件:"+sb.toString());
       System.out.println("读取文件" + absolutePath + "成功");      
   }
 
   //括号匹配：匹配CREATE TABLE左括号后对应的右括号
   public static int match(int index,String str) {
  Stack<Character> st = new Stack<Character>();
  Stack<Integer> num = new Stack<Integer>();
  int result = -1;
     for(int i=0;i<str.length();i++) {
     if(st.isEmpty()) {
      if(str.charAt(i)=='(' || str.charAt(i)==')') {
       st.push(str.charAt(i));
       num.push(i);
       i++;
      }
     }
     char temp = 0;
     if(!st.isEmpty()) {
          temp = st.peek();
          //System.out.println("st.peek():"+st.peek());
     }         
     if(temp=='('&&str.charAt(i)==')') {
      int tnum = num.peek();
      //System.out.println("num.peek():"+num.peek());
         num.pop();
      if(tnum==index) {
       result = i;
       break;
      }
     }else {
      if(str.charAt(i)=='(' || str.charAt(i)==')') {
       st.push(str.charAt(i));
          num.push(i); 
      }     
     } 
    }
    return result;     
   }

   //将某个文件的内容写入修改后的文件
   public static void writefile(String absolutePath,String delebegin,String deleend,String repbegin,String repend,String repstr) throws IOException {  
     String str = sb.toString();
     //写入新文件的过程中对文件进行的删除操作 
     String table = "CREATE TABLE";
  int tableindex = str.indexOf(table);//中间的参数为要删除字段的开始部分
  int braleftindex = -1;
  if(tableindex!=-1) {
  int beginindex = tableindex+table.length();
  String mulstr = str.substring(beginindex);
  braleftindex = str.indexOf("(");    
  System.out.println("sqlindex" + tableindex +"空格"+mulstr.substring(beginindex, beginindex+5)+"空格"+braleftindex);
  }  
  int brarightindex = match(braleftindex,str);
  System.out.println("brarightindex:"+brarightindex);
  String result = sb.toString();
  String finalresult =result;
  if(brarightindex!=-1) {
  //CREATE TABLE(……)后面的内容
  String mul2str = str.substring(brarightindex);
  int index = mul2str.indexOf(delebegin);    
  if(index!=-1) {
    String delepart = mul2str.substring(index);
    //String sub = delepart.replaceAll("读[^的]*的", ""); 
    String sub = delepart.replaceFirst(delebegin+"[^"+deleend+"]*"+deleend, "");
    //result = str.substring(0, brarightindex)+mul2str.substring(0,index)+sub.trim(); 
    //str.substring(0, brarightindex)是CREATE TABLE(……)
    //mul2str是CREATE TABLE(……)后面的内容，mul2str.substring(0,index)是CREATE TABLE(……)后面，
    //PCTFREE之前的内容，sub是PCTFREE后面删除PCTFREE和Storage之后的内容
    result = str.substring(0, brarightindex)+mul2str.substring(0,index)+sub; 
   
   //删除LOB()
   String delebegin2 ="STORAGE";
   String regex = "LOB(.*?)STORE AS";
   String regex2 = "STORAGE(.*?)PCTVERSION [0-9]+";
   String prestr = "";
   Pattern pattern = Pattern.compile(regex);
   Matcher matcher = pattern.matcher(sub);
   if (matcher.find()) {
    System.out.println("sqlbia:"+matcher.group());
    prestr = matcher.group();
   }
   int sqlindex = sub.indexOf(prestr);
   if(sqlindex!=-1&&prestr!="") {
    int sqlbeginindex = sqlindex+prestr.length();
    if(sqlbeginindex!=-1) {
       String lobstr = sub.substring(sqlbeginindex);
       int sqlfinalindex = lobstr.indexOf(";");
       //System.out.println("sqlfinalindex:"+sqlfinalindex+" lobstr:"+lobstr);
       String finalsql = lobstr.substring(0, sqlfinalindex+1); 
       StringBuffer finalsql1  = new StringBuffer();
    // 生成 Pattern 对象并且编译一个简单的正则表达式"cat"
       Pattern pattern2 = Pattern.compile(regex2);
    // 用 Pattern 类的 matcher() 方法生成一个 Matcher 对象
    Matcher matcher2 = pattern2.matcher(finalsql);
    while(matcher2.find()){
    //此时sb为fatdogfatdog，cat被替换为dog,并且将最后匹配到之前的子串都添加到sb对象中
         matcher2.appendReplacement(finalsql1,"");
    }
    //此时sb为fatdogfatdogfat，将最后匹配到后面的子串添加到sb对象中
    matcher2.appendTail(finalsql1);
    //输出内容为fatdogfatdogfat
    //System.out.println("finalsql1:"+finalsql1.toString());
    String finalsql2 = finalsql1.toString().replaceAll(delebegin2+"[^"+deleend+"]*"+deleend, "");    
    result = str.substring(0, brarightindex)+mul2str.substring(0,index)+sub.substring(0,sqlbeginindex)+finalsql2.toString()+lobstr.substring(sqlfinalindex+1);
    sub = sub.substring(0,sqlbeginindex)+finalsql2.toString()+lobstr.substring(sqlfinalindex+1);
    //System.out.println("str.substring(sqlfinalindex):"+str.substring(sqlfinalindex)+"finalsql:"+finalsql);
    //System.out.println("finalsql2:"+result.substring(0,sqlbeginindex)+finalsql2.toString());
    }
    }
   
    //删除PARTITION
   /* int partindex = sub.indexOf("PARTITION BY RANGE");
    if(partindex!=-1) {
    int len = partindex+"PARTITION BY RANGE".length();
    String partition = sub.substring(len);
    int finalindex = partition.indexOf(")");
    String finalstr = partition.substring(finalindex);
    String finalsub = finalstr.replaceAll(delebegin+"[^"+deleend+"]*"+deleend+System.getProperty("line.separator"), "");
    result = str.substring(0, brarightindex)+mul2str.substring(0,index)+sub.substring(0,len)+partition.substring(0,finalindex)+finalsub; 
    }*/
    
    int partindex = sub.indexOf("PARTITION BY RANGE");
    if(partindex!=-1) {
    int len = partindex+"PARTITION BY RANGE".length();
    String partition = sub.substring(len);
    int finalindex = partition.indexOf(")");
    String finalstr = partition.substring(finalindex);
    int patrfinalindex = finalstr.indexOf(";");
    String partfinalstr = finalstr.substring(0,patrfinalindex+1);
    String finalsub = partfinalstr.replaceAll(delebegin+"[^"+deleend+"]*"+deleend+System.getProperty("line.separator"), "");
    result = str.substring(0, brarightindex)+mul2str.substring(0,index)+sub.substring(0,len)+partition.substring(0,finalindex)+finalsub+finalstr.substring(patrfinalindex+1); 
    }  
  }   
  }     
    //写入新文件的过程中对文件进行的删除操作        
   /* int index = str.lastIndexOf(delebegin);//中间的参数为要删除字段的开始部分
    String result = sb.toString();
    if(index!=-1) {
     String delepart = str.substring(index);
     //String sub = delepart.replaceAll("读[^的]*的", "");    
     String sub = delepart.replaceAll(delebegin+"[^"+deleend+"]*"+deleend, "");
     result = str.substring(0, index)+sub.trim();     
    } 
    String finalresult =result;*/
    
    //写入新文件的过程中对文件进行的替换操作
    int index2 = result.lastIndexOf(repbegin);//中间的参数为要替换字段的开始部分
    if(index2!=-1) {
     String reppart = result.substring(index2);
     String rep="";
     if(repbegin!="" && repend!="") {
     rep = reppart.replaceFirst(repbegin+"[^"+repend+"]*"+repend, repstr);   
     }    
   finalresult = result.substring(0, index2)+rep.trim();
    }   
    /*  File f = new File(filefolder); 
    if (!f.exists()) { 
     f.mkdirs(); 
    } */
    /*File writename = new File(absolutePath); // 相对路径，如果没有则要建立一个新的output.txt文件         
        if(!writename.exists()) {
         boolean flag = writename.createNewFile(); 
         if(flag==true) {
          System.out.println("文件创建成功");  
         }else {
          System.out.println("文件创建失败");  
         }         
        } 
    // 创建新文件  
       BufferedWriter out = new BufferedWriter(new FileWriter(writename));  
       out.write(result); // \r\n即为换行
       //out.write(finalresult); // \r\n即为换行
       //out.write(str2+"\r\n"); // \r\n即为换行
       out.flush(); // 把缓存区内容压入文件  
       out.close(); // 最后记得关闭文件  */       
       
       FileOutputStream fos = new FileOutputStream(absolutePath);
       OutputStreamWriter osr = new OutputStreamWriter(fos, "Shift_JIS"); //GBK替换成你文件内容使用的编码
       BufferedWriter br = new BufferedWriter(osr);
       br.write(result); // \r\n即为换行
       br.flush();
       br.close();
       osr.close();
       fos.close();

   }
}
