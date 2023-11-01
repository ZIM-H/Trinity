using UnityEngine;
using WebSocketSharp;
using System.Collections;
using UnityEngine.Networking;

public class WebSocketClientManager : MonoBehaviour
{
    private WebSocket webSocket;
    private bool isConnected;
    private string serverURL = "wss://k9b308.p.ssafy.io:8589"; // 웹소켓 서버 주소로 바꾸세요
    private string userId; // userId를 저장하는 멤버 변수

    private string apiUrl = "https://k9b308.p.ssafy.io/api/game/match/"; // 대상 URL로 바꾸세요.

    void Start()
    {
        isConnected = false;
        DontDestroyOnLoad(gameObject);
    }

    public void ToggleWebSocketConnection()
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
            // userId 초기화
            userId = PlayerPrefs.GetString("UserId"); // PlayerPrefs에서 userId를 가져옴
            Debug.Log("매칭 큐 등록 요청 보내기");
            StartCoroutine(SendGetRequest());
            Debug.Log("웹소켓 연결 시도");
            // 연결되어 있지 않은 경우 연결 시도
            webSocket = new WebSocket(serverURL);
            webSocket.OnOpen += (sender, e) =>
            {
                isConnected = true;
                Debug.Log("WebSocket connected");
                // 연결 후 userId를 서버로 보냅니다.
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

    IEnumerator SendGetRequest()
    {
        Debug.Log("webRequest 송신");
        using (UnityWebRequest webRequest = UnityWebRequest.Get(apiUrl+userId))
        {
            Debug.Log("요청 보내는 url:"+apiUrl+userId);
            // 요청을 보냅니다.
            yield return webRequest.SendWebRequest();
            if (webRequest.result == UnityWebRequest.Result.Success)
            {
                // 요청이 성공했을 때의 처리
                Debug.Log("HttpRequest successful");
            }
            else
            {
                // 요청이 실패했을 때의 처리
                Debug.LogError("HttpRequest failed: " + webRequest.error);
            }
        }
    }
}
