# easyintern
国际化插件
#### 介绍
> easyIntern是一款基于idea IDE的国际化插件，基于产品给出的excel国际化表单，灵活的对表单内的语言进行国际化的相关处理。主要特性如下
- 目前支持`<string>`标签和`<string-array>`标签。
- 该插件会基于表单给定的值默认给出对应的`name`值，且支持对`name`值和`value`值的编辑
- 插件默认会对空格和&等特殊字符进行替换处理
- 针对文本替换问题，插件会在写入文件前比对单一项的相似度，自动提取出可疑的冲突的`name`和`value`，让用户在页面进行编辑，当然你也可以直接生成新文件，复制粘贴，然后在studio的`strings-xx`文件中编辑

#### 截图：

![image.png](https://i.loli.net/2019/12/07/vXa9qNfh1IbLdJe.png)

#### 使用：
- clone代码后，配置一下插件启动即可，是以gradle构建的项目，如下配置：
![image.png](https://i.loli.net/2019/12/07/CE85eAuqm7XLxFH.png)
- excel的格式只需要国家名称在一行即可（后续考虑兼容一列的情况），如下
![image.png](https://i.loli.net/2019/12/07/Iyqtsol5O9pU1Yf.png)
- 在配置窗口中填入有效区域，输入excel和输出的路径等必要参数,如下图：
![image.png](https://i.loli.net/2019/12/07/zmKHSicv2WNRA5s.png)
需要注意的是，如果你选取的有效区域没有包含国家名称（这里成为头部）则不需要勾选包含头这一项
- 输入配置参数，点击ok，会弹出key和value的配置对话框，插件默认给出了key，如有必要你需要自己填写，如下
![image.png](https://i.loli.net/2019/12/07/XZk3oaNRfDBzt5H.png)
- 完成上步后，点击ok，就完事了

#### 高级：
##### 替换老string
- 如果配置参数中输出路径中已经存在将要输出的文件名，则插件会采取冲突替换策略，会弹出冲突解决对话框，如下：
![image.png](https://i.loli.net/2019/12/07/dv3hN1Yxreycza7.png)

你可以选择替换与不替换，在操作完成后，插件会输出操作后的`strings-xx.xml`文件，需要注意的是，string-xx.xml会替换之前老版本中所有的空行，<string-array>标签页会放到xml文件的的最后面
