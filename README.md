# 测试环境

- HBase 1.0.1.1
- ElasticSearch 2.4.3

# 使用Maven打包

```
mvn clean compile assembly:single
```

部署请参照：[通过HBase Observer同步数据到ElasticSearch](http://guoze.me/2015/04/23/hbase-observer-sync-elasticsearch/)
