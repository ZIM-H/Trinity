using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class FertilizerRoomCount : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        GetComponent<TextMeshProUGUI>().text = "X " + VariableManager.Instance.fertilizerAmountInRoom;
        
    }

    // Update is called once per frame
    void Update()
    {
    }
    public void UseFertilizerUI(){
        GetComponent<TextMeshProUGUI>().text = "X " + (VariableManager.Instance.fertilizerAmountInRoom-1);
    }
}
