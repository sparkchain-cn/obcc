
### V1.4Bata1
1、删除balance本地的相关代码，解决其死锁的问题
2、进行一些简单的性能优化（回调采用简单的队列）
3、把写流水表的方式改成异步，创建钱包不回滚，解决更新死锁问题
4、井通java-lib的连接不上的出错信息

### v1.4Bata4
优化junction ,一个junction jar可以经过N个同质的链
去掉Test的junction，统一起来

### v1.4Bata5
离线签名（eth)
createToken gas 过低
server node

### v1.4Bata6
井通java溢出
修改sharding-jdbc to druid jdbc
解决日志异步时间先后的问题
加上井通的异常日志

### v1.4Bata7
transfer使用异步调用junction的相关方法
junction调用链时返回时，把数据写到redis
网关等待并从redis取到junction的相关方法异步回写的值
清除不用的表
把创建钱包中激活修改为从数据库中取相关配置来修改
修改井通的sequence的
###v.1.4.1Beta1
accessToken验证的代码剥离出来，加上兼容配置
加上读写分离

###v.1.4.2Beta1
调整Junction的接口
修改钱包余额查询
remove junction SendText接口
调整和重构Jingtum lib的代码


###v.1.4.2Beta2
调整Junction的依赖关系，从junction-interface中除去chain-end-config的依赖，需要使用数据库配置，在boot项目中引用chain-end-config。


###v.1.5.1Beta1
 eth java 版本
 moac java  版本
 修改回调流程,采用统一的订阅处理
 增加接口 getNonce
 增加接口 getHash
 增加接口
 
 ###v.1.6.0Beta1
 在sc_wallet加上一个额外信息的字段
 在sc_wallet_cb加上一些字段，用来进行新的状态的处理