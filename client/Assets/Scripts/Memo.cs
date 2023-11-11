using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using TMPro;

public class Memo : MonoBehaviour, IPointerClickHandler
{
    // Start is called before the first frame update
    void Start()
    {
        transform.GetChild(1).GetComponent<TextMeshProUGUI>().text = "소";
    }

    public void OnPointerClick(PointerEventData eventData){
        Debug.Log("clicked");
        TouchScreenKeyboard.Open("", TouchScreenKeyboardType.Default);
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
