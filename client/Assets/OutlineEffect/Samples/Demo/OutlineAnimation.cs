using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using cakeslice;

namespace cakeslice
{
    public class OutlineAnimation : MonoBehaviour
    {
        bool pingPong = false;

        // Use this for initialization
        void Start()
        {

        }

        // Update is called once per frame
        void Update()
        {
            Color c = GetComponent<OutlineEffect>().lineColor0;
            Color c1 = GetComponent<OutlineEffect>().lineColor1;
            Color c2 = GetComponent<OutlineEffect>().lineColor2;

            if(pingPong)
            {
                c.a += Time.deltaTime;
                c1.a += Time.deltaTime;
                c2.a += Time.deltaTime;

                if(c.a >= 1)
                    pingPong = false;
            }
            else
            {
                c.a -= Time.deltaTime;
                c1.a -= Time.deltaTime;
                c2.a -= Time.deltaTime;

                if(c.a <= 0)
                    pingPong = true;
            }

            c.a = Mathf.Clamp01(c.a);
            c1.a = Mathf.Clamp01(c1.a);
            c2.a = Mathf.Clamp01(c2.a);
            GetComponent<OutlineEffect>().lineColor0 = c;
            GetComponent<OutlineEffect>().lineColor1 = c1;
            GetComponent<OutlineEffect>().lineColor2 = c2;
            GetComponent<OutlineEffect>().UpdateMaterialsPublicProperties();
        }
    }
}