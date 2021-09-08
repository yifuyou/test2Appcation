package com.base.common.model.bean.xml.xmlBaseResponse;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


//<Etrack_ProcInterfaceResult>0</Etrack_ProcInterfaceResult>
//<retXml><?xml version="1.0" encoding="gb2312"?>
@Root(name = "Etrack_ProcInterfaceResponse")
public class RoleInfoResponse {

    @Element(name = "Etrack_ProcInterfaceResult")
    public String Etrack_ProcInterfaceResult;


//    @Element(name = "retXml")
//    public List<DoorPatientInfoBeen> retXml;
//    @Element(name = "retXml")
//    public String retXml;
    @Element(name = "retXml")
    public String retXml;

    @Override
    public String toString() {
        return "RoleInfoResponse{" +
                "Etrack_ProcInterfaceResult='" + Etrack_ProcInterfaceResult + '\'' +
                ", retXml=" + retXml +
                '}';
    }
}
