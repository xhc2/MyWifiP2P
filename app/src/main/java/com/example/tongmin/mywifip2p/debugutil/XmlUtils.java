package com.example.tongmin.mywifip2p.debugutil;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ͨ���෴�佫���е�����ȫ������xml�ļ���
 * xml������ʽ��pull��ʽ��ռ���ڴ�
 * Ŀǰ���ܶ�̬������Բ���
 *
 */
public class XmlUtils {


    private static XmlUtils o = new XmlUtils();
    Reflection reflection = new Reflection();

    private XmlUtils() {
    }

    ;

    public static XmlUtils getInstance() {
        if (o != null) {
            return o;
        }

        return null;
    }

    public List<Object> getObjectFromXml(InputStream in, Class clazz) throws Exception {
        List<Object> list = new ArrayList<Object>();


        String className = reflection.justGetName(clazz);
        list = new ArrayList<Object>();

        List<String> listProperty = reflection.getAllProperty(clazz.getName());

        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(in, "UTF-8"); //ΪPull����������Ҫ������XML����
        int event = pullParser.getEventType();
        Object object = null;
        while (event != XmlPullParser.END_DOCUMENT) {

            switch (event) {

                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if (className.equals(pullParser.getName())) {

                        object = reflection.newInstance(clazz.getName());

                    }
                    for(String str :listProperty){
                        /*�ҵ������˺� ��������ע��*/
                       if(str .equals(pullParser.getName())){
                           reflection.setField(object,str,pullParser.next());
                           break;
                       }
                    }


                    break;

                case XmlPullParser.END_TAG:
                    if (className.equals(pullParser.getName())) {
                        list.add(object);
                    }
                    break;

            }

            event = pullParser.next();
        }


        return list;
    }

    public void save(Class clazz , List<Object> list, OutputStream out) throws Exception {
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(out, "UTF-8");
        serializer.startDocument("UTF-8", true);
        serializer.startTag(null, "persons");

        List<String> listProperty = reflection.getAllProperty(clazz.getName());
        String className = reflection.justGetName(clazz);
        for(Object o : list){
            serializer.startTag(null, className);
            for(String str : listProperty){
                serializer.startTag(null, str);
                serializer.text(reflection.getField(o, str).toString());
                serializer.endTag(null, str);
            }
            serializer.endTag(null, className);
        }

//        for (Person person : persons) {
//            serializer.startTag(null, "person");
//            serializer.attribute(null, "id", person.getId().toString());
//            serializer.startTag(null, "name");
//            serializer.text(person.getName().toString());
//            serializer.endTag(null, "name");
//            serializer.startTag(null, "age");
//            serializer.text(person.getAge().toString());
//            serializer.endTag(null, "age");
//            serializer.endTag(null, "person");
//        }

        serializer.endTag(null, "persons");
        serializer.endDocument();
        out.flush();
        out.close();
    }

}

