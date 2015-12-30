﻿///
/// [SIMINOV FRAMEWORK - CONNECT]
/// Copyright [2014-2016] [Siminov Software Solution LLP|support@siminov.com]
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.




using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Siminov.Connect.Exception
{


    /// <summary>
    /// It exposes APIs to Get and Set sync exception information
    /// </summary>
    public class SyncException : Core.Exception.SiminovException
    {

        /// <summary>
        /// SyncException Constructor
        /// </summary>
        /// <param name="className">Name of class</param>
        /// <param name="methodName">Name of method</param>
        /// <param name="message">Exception message</param>
	    public SyncException(String className, String methodName, String message) 
            : base(className, methodName, message)
        {
		    
	    }

    }
}
