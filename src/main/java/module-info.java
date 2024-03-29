// <editor-fold desc="The MIT License" defaultstate="collapsed">
/* 
 * The MIT License
 * 
 * Copyright 2021 Studio 42 GmbH ( https://www.s42m.de ).
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
//</editor-fold>

module de.sft.dlui
{
	requires java.desktop;
	requires de.sft.dl;
	requires de.sft.log;
	requires de.sft.base;
	
	exports de.s42.dl.ui;
	exports de.s42.dl.ui.actions;
	exports de.s42.dl.ui.actions.web;
	exports de.s42.dl.ui.annotations;
	exports de.s42.dl.ui.bindings;
	exports de.s42.dl.ui.components;
	exports de.s42.dl.ui.components.button;
	exports de.s42.dl.ui.components.dropdown;
	exports de.s42.dl.ui.components.menu;
	exports de.s42.dl.ui.components.window;
	exports de.s42.dl.ui.events;
	exports de.s42.dl.ui.pragmas;
	exports de.s42.dl.ui.visual;
}
