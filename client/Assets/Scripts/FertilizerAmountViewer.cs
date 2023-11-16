using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class FertilizerAmountViewer : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        gameObject.GetComponent<TextMeshPro>().text = VariableManager.Instance.fertilizerAmount + "/4";
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
