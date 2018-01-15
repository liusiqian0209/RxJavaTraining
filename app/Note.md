## 操作符

### Creating Observables

##### Create
##### Just
> 相当于简化版的Create

```
Observable.just("String").subscribeOn(Schedulers.io());
```
##### From
> 将数组或者List转换为 Observable
##### Defer
> 在调用subscribe之后，才创建Observable（延迟创建对象）
##### Empty / Never / Throw
> Empty 创建没有任何数据项的Observable，subscribe时直接回调onComplete

> Never 没有任何数据项的Observable，也不会终止

> Throw 创建没有任何数据项的Observable，subscribe时直接回调onError
##### interval
> 每间隔一段时间发射一个数据项
##### Range
> 创建一个特定范围内的整数序列项 range(start, count)
##### Repeat
> 多次发射同一个数据项
##### Start
##### Timer
> 延迟一段时间后，发射数据项


### Transforming Observables
##### map
> 将一个item 按照指定规则转换为另一个item
##### flatMap
> 将一个item 按照指定规则转换为多个item
##### groupBy
> 将多个item根据指定分组规则，分成若干个组
##### buffer
> 将指定个数相邻的item合并为一个item
##### Scan
> 类似于python中的reduce ，每次把当前运算结果再发射出去
##### Window
> 根据一个时间间隔，将期间的事件合并为一个Observable发射出去

### Filtering Observables
##### debouce
> only emit an item from an Observable if a particular timespan has passed without it emitting another item.
##### distinct
> 去掉重复的数据项
##### ElementAt
> 取出指定位置的数据
##### filter
> 指定过滤条件，返回true则通过
##### first
> 返回第一个item
##### ignoreElements
> 不发射数据项，只发射onComplete 和 onError
##### last
> 类似于first 取最后一项
##### sample
> 对数据源定时采样
##### skip
> 跳过数据源中的前n项
##### skipLast
> 跳过数据源中的最后n项
##### take
> 取出数据源中的前n项
##### takeLast
> 取出数据源中的最后n项
### Combing Observables
### Error Handling Operators