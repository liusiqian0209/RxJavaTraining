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
##### zip
> 多个数据源组合成一个数据源,当其中一个Observable发射数据项结束或者出现异常后，另一个Observable也将停止发射数据
##### merge
> 多个数据源合并成一个数据源，事件数为之前所有数据源的数目之和（不组合item）
##### startWith
> 在原先数据源之前插入新的item或者新的Observable
##### combineLatest
> 将两个数据源合成一个数据源，每当某一个数据源发射一个事件时，它总是与其他数据源最新发射的那个数据项组合成为一个新的数据项
##### join
> 将两个数据源合并成一个数据源，每当某一个数据源发射一个事件时，在之后的一段时间内，另一个数据源如果发射的事件都会和这个数据项合并成为一个新的数据项
##### switchOnNext
> 将一个发射多个Observable的数据源转换成一个单独的Observable，如果之前的Observables在发射的数据时时间上有重叠，仅保留最新的Observable发射的数据项


### Error Handling Operators
##### catch
> onErrorReturn 处理onError时，返回一个数据项，之后以onComplete结束数据流
> onErrorResumeNext 处理onError时，使用正常的数据流代替错误流
> onExceptionResumeNext 当Error是一个Exception时，使用正常的数据流代替错误流
##### retry
> retry 当错误发生时，尝试跳过这个数据项，然后继续流中其他的数据项直到终止
> retryWhen 当错误发生时，先跳过这个数据项，继续处理流中其他的数据项，在指定的延迟时间后再重新处理这个错误项