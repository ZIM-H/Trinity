using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class WorkLimitShow : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        if(VariableManager.Instance.workLimit == 1){
            GetComponent<Image>().color = Color.black;
        }
    }
}
