package com.base.common.model.bean.xml.xmlBaseRequest;

/**
 * Created by sinyo on 2017/7/27.
 */

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name = "soap:Body", strict = false)
public class RoleInfoRequestBody {
    @Element(name = "Etrack_ProcInterface", required = false)
    public RoleInfoBaseRequest getroleinfo;

    @Override
    public String toString() {
        return "RoleInfoRequestBody{" +
                "getroleinfo=" + getroleinfo +
                '}';
    }
}
