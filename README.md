项目名称：战弈象棋

项目描述：
该项目是一个基于springBoot开发的中国象棋游戏网页版、实现了玩家匹配、实时对战等功能并添加了一个新的玩法，加入类似云顶之奕的海克斯，在固定回合为棋子选择一种强化，被强化的棋子和强化的规则可以自行扩展，也可自行扩展棋子及其规则。

特性：
1.基于websocket的实时通信
2.棋子规则类型的可扩展性
3.基于JWT+Redis实现用户校验及登录模块的安全性

演示：
游戏主界面
<img width="2560" height="1496" alt="屏幕截图 2025-10-18 221156" src="https://github.com/user-attachments/assets/bd37d559-f513-41bd-ad05-daa83ccfe59f" />
单人练习界面
<img width="2560" height="1496" alt="屏幕截图 2025-10-18 221243" src="https://github.com/user-attachments/assets/449fc049-eef8-4f7d-8d0c-3641c7be13e9" />
新模式的界面
<img width="2538" height="1564" alt="屏幕截图 2025-10-18 221449" src="https://github.com/user-attachments/assets/f7a5ec11-466e-4728-9bd7-84b4c20001e9" />
强化界面展示
<img width="2526" height="1442" alt="屏幕截图 2025-10-18 221512" src="https://github.com/user-attachments/assets/f7a7d7a1-2086-468a-927d-ee12bac64e83" />
强化后棋子移动路径展示（小绿点就是可移动的路径）
<img width="576" height="516" alt="屏幕截图 2025-10-18 221548" src="https://github.com/user-attachments/assets/1e25c23d-f202-4778-b3f6-43f2e206793f" />



快速开始：
本项目SpringBoot版本为3.5.5；
Java版本为17所以请用Java17及以上环境运行项目；
mysql 8.0及以上；
redis 6.0及以上；
涉及到依赖均在pom.xml文件中；

运行前请把application.yml文件中的mysql的驱动和redis的连接换成自己的；
关于数据库中的表和数据请在自己的数据库中运行zhanyi_chess.sql文件；
做完这些后，即可启动项目并访问localhost:8081来打开





