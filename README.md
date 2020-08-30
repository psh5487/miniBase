# minibase
SWE3033 데이터베이스 실습 - minibase 프로젝트

Minibase는 Wisconsin 대학에서 교육적인 목적을 위하여 만든 Database Management System (DBMS)이다. Minibase는 parser, optimizer, buffer pool manager, heap file, B+-tree index, disk space management system으로 구성되어 있으며, 각각의 component 별로 독립적인 학습이 가능하도록 설계되어 있다. Minibase는 DBMS 전체를 구현하는 수고 없이 각각의 component를 구현해 볼 수 있으므로, DBMS의 내부적인 구조를 이해하는 데에 적합하다.

## minibase Structure
### Logical Abstraction
<img width="600" src="https://user-images.githubusercontent.com/26567880/91587109-48da0980-e991-11ea-8663-da171cb002ed.png">

### Physical Abstraction (Page, File)
<img width="800" src="https://user-images.githubusercontent.com/26567880/91587122-4e375400-e991-11ea-8f77-1a35855523ba.png">

### Architecture
<img width="550" src="https://user-images.githubusercontent.com/26567880/91587416-b423db80-e991-11ea-9616-fe1dc0be433c.png">

## Lab Assignment
1. Fields, Tuples, Catalog
2. BufferPool
3. HeapFile Mutability, Sequential Scan, Insertion and Deletion, Eviction
4. Filter, Predicate
5. Join
6. Aggregator
7. Lock
8. Join Optimizer, Table, Stats, Histogram

### Development Environment 
- Linux 

### Prerequisite
#### java
```bash
sudo apt-get install openjdk-[version]-jdk
```

#### ant 
what is ant? [link](http://ant.apache.org/) 
```bash
$ sudo apt-get install ant 
```

## Command
|command|description|
|----------|-----------------------------------------|
|ant|Build miniBase|
|ant test|Run all the unit test provided|
|ant runtest -Dtest=testname|Run the specific unit test|
|ant systemtest|Run all the unit test provided|
|ant run systest -Dtest=testname|Run the specific unit test|
|ant clean|Remove binary files|
|and handin|Make tarball|



