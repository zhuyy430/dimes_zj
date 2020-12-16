<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>


<div data-toggle="topjui-layout" data-options="fit:true">
    <div
            data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
        <form id="ff" method="post">
            <div title="编辑" data-options="iconCls:'fa fa-th'">
                <div class="topjui-fluid">
                    <div style="height: 70px"></div>
                    <div class="topjui-row">
                        <div class="topjui-col-sm10">
                            <label class="topjui-form-label">使用频次</label>
                            <div class="topjui-input-block">
                                <input type="text" name="useFrequency" data-toggle="topjui-numberbox"
                                       data-options="required:false" id="useFrequency">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>