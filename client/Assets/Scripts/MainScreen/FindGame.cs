using UnityEngine;
using WebSocketSharp;

public class FindGame : MonoBehaviour
{
    private WebSocket webSocket;
    private bool isConnected;

    void Start()
    {
        isConnected = false;
    }

    public void ToggleWebSocketConnection()
    {
        if (isConnected)
        {
            // 이미 연결된 경우 연결 종료
            webSocket.Close();
            isConnected = false;
        }
        else
        {
            // 연결되어있지 않은 경우 연결 시도
            string url = "wss://abc.com:8589"; // WebSocket 서버 주소로 바꾸세요
            webSocket = new WebSocket(url);
            webSocket.OnOpen += (sender, e) => isConnected = true;
            webSocket.OnError += (sender, e) => Debug.LogError("WebSocket error: " + e.Message);
            webSocket.ConnectAsync();
        }
    }
}
