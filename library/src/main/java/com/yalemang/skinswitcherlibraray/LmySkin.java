package com.yalemang.skinswitcherlibraray;

import com.yalemang.skinswitcherlibraray.exception.SkinFileFormatException;
import com.yalemang.skinswitcherlibraray.exception.SkinFileNotFoundException;

import java.io.File;

/**
 * 皮肤Bean类
 */
public class LmySkin {
    //皮肤对应的本地路径
    private String path;
    //皮肤对应的名字，如果不进行设置，则为文件名
    private String name;

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
