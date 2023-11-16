using System.Collections;
using System.Collections.Generic;
using System.Reflection;
using UnityEngine;
using UnityEngine.EventSystems;

public class IPointerExample : MonoBehaviour, IPointerClickHandler
{
    
    public void OnPointerClick(PointerEventData eventData){
        Debug.Log("yes");
    }
}
