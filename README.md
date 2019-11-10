![banner](https://i.loli.net/2019/11/10/eP1rYN4gpomilHS.png)


# 点歌Mod(某云api使用)

## 可能的配置需求
* 因此mod代码质量，额外需要512MB~1024MB内存进行解码和播放操作
* 因此mod项目依赖质量，下载mod需要额外的网络流量
* 因fabricAPI熟悉程度问题，部分操作可能难受，欢迎反馈
* 本mod使用FFMPEG解码，jar文件已可能打包大部分平台的库

### 功能
* 显示歌词
* 更改pitch(无用)
* 拖动进度条
* 支持直接输入某云歌曲id进行点歌播放

### 命令
/yysplayer play \<s> 从网易云点歌  
/yysplayer gain 改音量(0.0\~1.0)  
/yysplayer play 修改pitch(0.0\~2.0)  
/yysplayer info 查看当前播放情况  
/yysplayer seek <second> 拖动进度条(单位为秒)  
/yysplayer toggle <状态> 切换状态(pause/lyrics)

### 未来可能的更新
* 使用OpenAL来允许更多奇怪参数设置 (谁会动)
* 播放本地音乐 (为什么不直接开**云)

# 警告:
返回主菜单歌曲还会继续播放



### 附录:
mod开发参考文档:  
Fabric开发文档翻译   
https://www.mcbbs.net/thread-904854-1-1.html   
(出处: Minecraft(我的世界)中文论坛)


(最后:有意思是因为改pitch (谁无聊会改呢))