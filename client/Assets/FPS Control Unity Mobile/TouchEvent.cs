using System.Collections;
using System.Collections.Generic;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.EventSystems;

public class TouchEvent : MonoBehaviour, IPointerClickHandler
{
    public bool condition = false;
    
    void Start()
    {
        condition = false;
    }
    public void OnPointerClick(PointerEventData eventData){
        condition = true;
        Debug.Log(condition);
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
