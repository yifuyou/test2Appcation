package com.base.common.model.bean;

import java.io.Serializable;

public class KeyValue<K, V, D> implements Serializable {

    private K key;
    private V value;
    private D desc;

    public KeyValue() {

    }

    public KeyValue(K key, V value, D desc) {
        this.key = key;
        this.value = value;
        this.desc = desc;
    }


    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public D getDesc() {
        return desc;
    }

    public void setDesc(D desc) {
        this.desc = desc;
    }


    public boolean isEmptyValue() {
        return value == null;
    }
}
