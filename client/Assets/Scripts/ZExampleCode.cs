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
    }

    // Update is called once per frame
    void Update()
    {
    }
}
