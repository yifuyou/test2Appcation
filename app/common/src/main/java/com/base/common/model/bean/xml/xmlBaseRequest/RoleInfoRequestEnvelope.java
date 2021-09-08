package com.base.common.model.bean.xml.xmlBaseRequest;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;


//<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
@Root(name = "soap:Envelope")
@NamespaceList({
        @Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "xsi"),
        @Namespace(reference = "http://www.w3.org/2001/XMLSchema", prefix = "xsd"),
        @Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap")
})
public class RoleInfoRequestEnvelope {
    @Element(name = "soap:Body")
    public RoleInfoRequestBody body;

    @Element(name = "soap:HeaderBean", required = false)
    public String aHeader;

    @Override
    public String toString() {
        return "RoleInfoRequestEnvelope{" +
                "body=" + body +
                ", aHeader='" + aHeader + '\'' +
                '}';
    }
}
