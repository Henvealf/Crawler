# Crawler
这是一个爬虫工具，用正则表达式实现一些标签匹配与得到指定标签中的内容。
并封装了一下数据（记录）生成的过程，提供了使用IP代理的功能，现在只支持免费代理（不过现在并不能抓到网页，不知是代理问题还是程序问题）。

## 适用范围

暂时只适应与网站的分页链接为规整可以拼接而成（大部分网站都是这样）。例如 http://www.example.com/things/n (n为页码数)。

无法获取由 Js 之后生成的内容。

## 一些简单要求

当你将你的每条数据（即记录）的属性封装成一个实体类时，请务必实现他的 toString() 方法，并在其中设置你想要的属性分割字符，且在最后不要加换行符号。

## 简单介绍

在 **com.henvealf.crawler** 包中有三个类：

* #### RegexStr

  该类中放置了用于生成（其实就是字符串拼接）正则表达式的方法，具体在注释中都有。
* #### HtmlParse

  其中就是完成 RegexStr 类中正则表达式的匹配工作，都是一些很简单的方法。

* #### HtmlDataCreater

  看名字就是 Html 中的数据的生成器。类的功能就是解析Html文本的内容，并将解析后的记录写到文件中。

  这里使用了模板的设计模式，需要继承他，实现其中的抽象方法后，就可以调用 **writeDataToFile** 方法来进行页面分析与数据写入，一共有三个参数，注释中有解释，这里就不废话了。

  下面说说需要实现的抽象方法：

  - **generateUrl(String baseUrl, int page)**

    方法完成的功能是根据页码数来生成相应的网页连接，第一个参数就是你在 **writeDataToFile** 中传入的基础链接，而第二参数就代表当前所在的页码数。

  - **generateItemList((HtmlParser htmlParse))**

    方法需要用户实现的就是用已经封装了页面字符串的 HtmlParser 对象,来从中得到想要的数据并封装成代表一条记录的对象，因为一个页面会有许多条对象，所以你需要自己将他们封装成一个 List 集合，并返回。

  这部分比较繁琐，请耐心，还有要对没条属性的不同情况进行相应的处理，在抓取拉勾网的示例中就会看到。

##### 关于 ip 代理

  为了更方便的使用 ip 代理功能，这里提供了几个方法。

  **useIpProxy()** 使用该方法来启用代理功能。

  **setIpProxyMap()** 使用该方法填入代理信息，参数 Map 的 Key(IP) 要求为 String, Value(Port) 要求为 int 。

  **setPerProxyPageCount()** 使用该方法设置每个 ip 抓取的页面个数，默认为 20 页。

  > 注意，只有启用了代理功能，代理map 不为空（指针与内容）两个条件同时成立时，代理功能才会真正被启用。

在 **com.henvealf.http** 包中有两个类：

* #### HeGet

  该类中封装了使用 Java 原生的 URLConnection 类实现的 GET 请求模拟。
* #### HePost
  
  这就是 POST 请求的模拟，暂时没用到，所以没有内容。

## 示例

在 **com.henvealf.crawler.example** 包中为爬取页面的例子。

* #### ipproxy
  
  该例子前面提到了网站链接为： http://www.kuaidaili.com/free/inha/ ， 使用 FindIpProxy 类中的 getAllIpProxy(int maxPage) 方法来抓取代理信息，参数为要抓取的页数。
  该示例示例的是最基础的两个类 **RegexStr** 与 **HtmlParse** 来完成的。
  因为该网站的分页连接格式为固定形式，所以可以直接在链接中指定页码。
  测试代码在 test 目录中的 test_IpProxyCrawler 中。

* #### 拉钩网（lagou）

  在上例的基础上，对解析以及数据写入进行了封装。本例就使用的是封装后的工具来完成的。测试类为 **LagouTest** 。
  普通的抓取测试方法为 **test_LgDataCreater()**.
  使用了代理的测试方法为: **test_LgDataCreater_use_proxy**。

  > 在我写这个示例的时候，昨晚对网页分析并抓取数据成功了，第二天回来竟发现分析和昨晚不一样了，一看原来网页改版了，汗！。

#### 使用或转载请注明出处，Thanks, END!!


