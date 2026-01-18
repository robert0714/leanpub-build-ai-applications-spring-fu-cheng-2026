# Issues: 
```powershell
netsh interface ipv4 show excludedportrange protocol=tcp | Select-String -Pattern "9200|9199|9201" -Context 1,1

netsh interface ipv4 show excludedportrange protocol=tcp

通訊協定 tcp 連接埠排除範圍

開始連接埠    結束連接埠
----------    ----------
      1073        1172      
      1196        1295
      1296        1395
      1396        1495
      2536        2635
      2636        2735
      5357        5357
      9009        9108
      9109        9208
      9209        9308
      9309        9408
      9409        9508
     10555       10654
     28385       28385
     28390       28390
     50000       50059     *

* - 管理的連接埠排除。
Port 9200 in Windows Hyper-V 保留的端口範圍內（9209-9308 包含了 9200）。
```

* Method 1 :
  ```powershell
  net stop winnat
  net start winnat
  ```
* Method 2 :
  ```powershell
  net stop winnat
  # 將 9200 排除在動態分配之外，確保它能被你的 Docker 使用
  netsh int ipv4 add excludedportrange protocol=tcp startport=9200 numberofports=1
  netsh int ipv4 add excludedportrange protocol=tcp startport=9300 numberofports=1
  net start winnat
  ```
* Method 3 :
  ```powershell
  netsh int ipv4 show dynamicport tcp
  # 將 9200 排除在動態分配之外，確保它能被你的 Docker 使用
  netsh int ipv4 set dynamic tcp start=9200 num=200
  ```
* Method 4 :
```powershell
docker-compose up -d
Start-Sleep -Seconds 15; curl http://localhost:19200

docker logs elasticsearch-node --tail 20
```

# Usage
* swagger: http://localhost:8063/swagger-ui/index.html#/cooking-agent-controller/chat
* swagger: http://localhost:8080