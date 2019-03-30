# NogizakaPlugin
一個比陽春麵還陽春的乃木戀外掛

基本原理上使用螢幕顏色判別，每個球為一個物件，利用遞迴找出三顆(含)以上的路徑，然後控制滑鼠消珠

<br>測試環境
* Windows10 + BlueStacks(執行app)視窗最大化


<br><strong>目前尚未解決的問題 :</strong> 
* 球的顏色判斷錯誤時不會自動選擇第二路徑
* 速度太慢，應改寫成多執行緒，但須注意資源互搶問題
* 技能施放
* 螢幕像素點位置是根據我的螢幕寫死，真的要用要有一套調整機制

![](https://github.com/tks3589/NogizakaPlugin/blob/master/testP/show.gif)


