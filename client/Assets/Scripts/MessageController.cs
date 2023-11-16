using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class MessageController : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        transform.Find("InteractiveUIPanelForMemo").GetChild(0).GetChild(1).GetChild(0).Find("Placeholder").gameObject.GetComponent<TextMeshProUGUI>().text
         = VariableManager.Instance.message;
    }

    // Update is called once per frame
    void Update()
    {
        
    }
    public void MessageSend(){
        if(transform.Find("InteractiveUIPanelForMemo").GetChild(0).GetChild(1).GetChild(0).Find("Text").gameObject.GetComponent<TextMeshProUGUI>().text != VariableManager.Instance.message)
        VariableManager.Instance.message = transform.Find("InteractiveUIPanelForMemo").GetChild(0).GetChild(1).GetChild(0).Find("Text").gameObject.GetComponent<TextMeshProUGUI>().text;
    }



}
