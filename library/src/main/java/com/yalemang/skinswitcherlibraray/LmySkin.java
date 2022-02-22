package com.yalemang.skinswitcherlibraray;

import java.io.File;

/**
 * 皮肤Bean类
 */
public class LmySkin {
    //皮肤对应的本地路径
    protected String path;
    //皮肤对应的名字
    protected String name;

    public LmySkin(String name, String path) {
        this.path = path;
        this.name = name;
    }

    public LmySkin(String path){
        this.path = path;
        this.name = new File(path).getName();
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
