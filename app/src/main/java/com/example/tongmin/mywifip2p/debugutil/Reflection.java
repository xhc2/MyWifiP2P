package com.example.tongmin.mywifip2p.debugutil;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/23.
 */
public class Reflection {
    /**
     * 获取指定的属性
     *
     * @param owner, fieldName
     * @return �����Զ���
     * @throws Exception
     *
     */
    public Object getProperty(Object owner, String fieldName) throws Exception {
        Class ownerClass = owner.getClass();

        Field field = ownerClass.getField(fieldName);

        Object property = field.get(owner);

        return property;
    }



    /**
     * 获取静态的属性
     *
     * @return
     * @throws Exception
     */
    public Object getStaticProperty(String className, String fieldName)
            throws Exception {
        Class ownerClass = Class.forName(className);

        Field field = ownerClass.getField(fieldName);

        Object property = field.get(ownerClass);

        return property;
    }

    /**
     * 获取全部属性
     * @param className
     * @return
     * @throws Exception
     */
    public List<String> getAllProperty(String className )  throws Exception{
        Class ownerClass = Class.forName(className);
        Field[] field = ownerClass.getDeclaredFields();
        List<String> listName = new ArrayList<String>();
        for(Field f : field){
            listName.add(f.getName());
        }
        return listName;

    }

    /**
     * 执行方法
     *
     * @param owner
     * @param methodName
     * @param args
     * @throws Exception
     */
    public Object invokeMethod(Object owner, String methodName, Object[] args)
            throws Exception {

        Class ownerClass = owner.getClass();

        Class[] argsClass = new Class[args.length];

        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }

        Method method = ownerClass.getMethod(methodName, argsClass);

        return method.invoke(owner, args);
    }


    /**
     * 执行静态方法
     *
     * @param className
     *            ����
     * @param methodName
     *            ������
     * @param args
     *            ��������
     * @return ִ�з������صĽ��
     * @throws Exception
     */
    public Object invokeStaticMethod(String className, String methodName,
                                     Object[] args) throws Exception {
        Class ownerClass = Class.forName(className);

        Class[] argsClass = new Class[args.length];

        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }

        Method method = ownerClass.getMethod(methodName, argsClass);

        return method.invoke(null, args);
    }

    /**
     * 仅仅获取一个类的名字。不是全类名
     * e.g util.xhc -> return xhc
     * @param clazz
     * @return
     */
    public String justGetName(Class clazz){

        String str =  clazz.getName();
        String[] clazzName = str.split("\\.");
        return clazzName[clazzName.length - 1];
    }

    /**
     * �½�ʵ��
     *
     * @param className
     *            ����
     * @param args
     *            ���캯���Ĳ���
     * @return �½���ʵ��
     * @throws Exception
     */
    public Object newInstance(String className, Object[] args) throws Exception {
        Class newoneClass = Class.forName(className);

        Class[] argsClass = new Class[args.length];

        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }

        Constructor cons = newoneClass.getConstructor(argsClass);

        return cons.newInstance(args);

    }

    /**
     * ����һ��ʵ�������캯��û�в���
     * @param className
     * @return
     * @throws Exception
     */
    public Object newInstance(String className) throws Exception {
        Class newoneClass = Class.forName(className);
        Constructor  cons = newoneClass.getConstructor();
        return cons.newInstance();
    }

    /**
     * ��ĳһ������ֱ�Ӹ�ֵ����ʹ��set����
     * @param ob1
     * @param fieldName
     * @param args
     * @throws Exception
     */
    public void setField(Object ob1,String fieldName , Object args) throws Exception{
        Field field = ob1.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(ob1, args);
    }

    public Object getField(Object ob1,String fieldName ) throws Exception{
        Field f = ob1.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return f.get(ob1);
    }

    /**
     * �ǲ���ĳ�����ʵ��
     * @param obj ʵ��
     * @param cls ��
     * @return ��� obj �Ǵ����ʵ�����򷵻� true
     */
    public boolean isInstance(Object obj, Class cls) {
        return cls.isInstance(obj);
    }

    /**
     * �õ������е�ĳ��Ԫ��
     * @param array ����
     * @param index ����
     * @return ����ָ��������������������ֵ
     */
    public Object getByArray(Object array, int index) {
        return Array.get(array, index);
    }
}
