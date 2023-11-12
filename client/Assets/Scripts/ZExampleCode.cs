using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;

public class ZExampleCode : MonoBehaviour, IPointerClickHandler
{// Start is called before the first frame update
    GameObject canvas;
    void Start()
    {
        canvas = GameObject.Find("Canvas");
    }

    public void OnPointerClick(PointerEventData eventData){
        canvas.transform.Find("InteractiveUIPanelForMemo").gameObject.SetActive(true);
        StartCoroutine(waittest());
    }

    IEnumerator waittest(){
        Debug.Log("첫 번째 메시지");
        yield return new WaitForSeconds(2.0f);
        Debug.Log("두 번째 메시지");
        yield return new WaitForSeconds(2.0f);
        Debug.Log("세 번째 메시지");
        yield return new WaitForSeconds(4.0f);
        Debug.Log("네 번째 메시지");
    }

    // Update is called once per frame
    void Update()
    {
    }
}
