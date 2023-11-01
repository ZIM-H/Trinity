using UnityEngine;
using WebSocketSharp;

public class WebSocketClientManager : MonoBehaviour
{
    private WebSocket webSocket;
    private bool isConnected;
    private string serverURL = "wss://k9b308.p.ssafy.io:8589"; // 웹소켓 서버 주소로 바꾸세요

    void Start()
    {
        isConnected = false;
        DontDestroyOnLoad(gameObject);
    }

    public void ToggleWebSocketConnection(string userId)
    {
        if (isConnected)
        {
            Debug.Log("웹소켓 연결 종료");
            // 이미 연결되어 있는 경우 연결 종료
            webSocket.Close();
            isConnected = false;
            Debug.Log("WebSocket disconnected");
        }
        else
        {
            Debug.Log("웹소켓 연결 시도");
            // 연결되어 있지 않은 경우 연결 시도
            webSocket = new WebSocket(serverURL);
            webSocket.OnOpen += (sender, e) =>
            {
                isConnected = true;
                Debug.Log("WebSocket connected");
                // 연결 후 userId를 서버로 보냅니다.
                string userId = PlayerPrefs.GetString("UserId"); // "YourKey"는 확인하려는 PlayerPrefs 키입니다.
                Debug.Log("저장된 userId: " + userId);
                SendUserId(userId);
            };
            webSocket.OnError += (sender, e) => Debug.LogError("WebSocket error: " + e.Message);
            webSocket.ConnectAsync();
        }
    }

    private void SendUserId(string userId)
    {
        // userId를 서버로 보내는 로직을 구현하세요.
        // 이 부분에서 웹소켓 메시지를 생성하고 서버에 전송합니다.
        // 예를 들어, JSON 형식으로 메시지를 만들어 보낼 수 있습니다.
        string jsonMessage = "{\"userId\": \"" + userId + "\", \"type\": \"matching\"}";
        Debug.Log("웹소켓 전송할 jsonMessage:" + jsonMessage);
        webSocket.Send(jsonMessage);
    }

}
