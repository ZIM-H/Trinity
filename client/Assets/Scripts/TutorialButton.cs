using System.Collections;
using System.Collections.Generic;
using System.Linq.Expressions;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.SceneManagement;
public class TutorialButton : MonoBehaviour, IPointerClickHandler
{
    public int buttonType;
    // Start is called before the first frame update
    GameObject webSocketManager;
    void Start()
    {
        webSocketManager = GameObject.Find("WebSocketManager");
    }
    
    public void OnPointerClick(PointerEventData eventData){
        webSocketManager.GetComponent<AudioSource>().Play();
        if(buttonType==2){
            StartCoroutine(goToHome());
        }else if(buttonType == 1){
            transform.parent.GetComponent<TutorialController>().GoRight();
        }else if(buttonType == 3){
            transform.parent.GetComponent<TutorialController>().GoLeft();
        }
    }
    IEnumerator goToHome(){
        webSocketManager.GetComponent<WebSocketClientManager>().ToggleWebSocketConnection();
        yield return new WaitForSeconds(0.3f);
        webSocketManager.GetComponent<WebSocketClientManager>().ToggleWebSocketConnection();
    }


    // Update is called once per frame
    void Update()
    {
        
    }
}
