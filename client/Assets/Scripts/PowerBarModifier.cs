using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using DG.Tweening;

public class PowerBarModifier : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        transform.DOScaleX(0.335f*VariableManager.Instance.power, 1.5f);
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
