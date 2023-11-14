using UnityEngine;
using UnityEngine.EventSystems;

public class IconTouch : MonoBehaviour, IPointerClickHandler
{
    public void OnPointerClick(PointerEventData eventData){
        Debug.Log("Icon Touched!");
        // 터치 이벤트에 대한 추가 동작을 여기에 구현
        WebSocketClientManager webSocketClientManager = GameObject.Find("WebSocketManager").GetComponent<WebSocketClientManager>();
        if (webSocketClientManager != null) {
            webSocketClientManager.ToggleScorePanel();
        } else {
            Debug.Log("웹소켓 클라이언트 매니저 찾을 수 없음");
        }
    }
}
