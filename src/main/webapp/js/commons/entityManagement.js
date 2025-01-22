/**
 * Generic Entity Management JavaScript Module
 */
var EntityManagement = (function($) {
    
    // Configuration object to be set by implementing pages
    var config = {
        listUrl: '',
        addUrl: '',
        updateUrl: '',
        deleteUrl: '',
        importUrl: '',
        exportUrl: '',
        columns: [],
        formFields: []
    };

    // Current state
    var state = {
        searchType: 'searchByID',
        currentId: null
    };

    // Initialize the module
    function init(customConfig) {
        // Merge custom config with defaults
        config = $.extend({}, config, customConfig);
        
        // Initialize bootstrap-table
        initTable();
        
        // Bind event handlers
        bindEvents();
    }

    // Initialize the data table
    function initTable() {
        $('#entityTable').bootstrapTable({
            url: config.listUrl,
            method: 'get',
            queryParams: function(params) {
                return {
                    searchType: state.searchType,
                    keyWord: $('#searchKeyWord').val(),
                    offset: params.offset,
                    limit: params.limit
                };
            },
            columns: config.columns
        });
    }

    // Bind all event handlers
    function bindEvents() {
        // Search type selection
        $('.searchTypeItem').click(function() {
            state.searchType = $(this).data('type');
            $('#searchTypeText').text($(this).text());
        });

        // Search button
        $('#searchBtn').click(function() {
            $('#entityTable').bootstrapTable('refresh');
        });

        // Add button
        $('#addEntityBtn').click(function() {
            state.currentId = null;
            showEntityModal('Add Entity');
        });

        // Edit button (delegated event for dynamic rows)
        $('#entityTable').on('click', '.edit-btn', function() {
            state.currentId = $(this).data('id');
            loadEntityData(state.currentId);
        });

        // Delete button (delegated event for dynamic rows)
        $('#entityTable').on('click', '.delete-btn', function() {
            if (confirm('Are you sure you want to delete this item?')) {
                deleteEntity($(this).data('id'));
            }
        });

        // Save button
        $('#saveEntityBtn').click(function() {
            saveEntity();
        });

        // Import button
        $('#importEntityBtn').click(function() {
            $('#importModal').modal('show');
        });

        // Export button
        $('#exportEntityBtn').click(function() {
            exportEntities();
        });

        // Confirm import button
        $('#confirmImportBtn').click(function() {
            importEntities();
        });
    }

    // Show entity modal with dynamic form fields
    function showEntityModal(title) {
        $('#entityModalTitle').text(title);
        var form = $('#entityForm');
        form.empty();
        
        config.formFields.forEach(function(field) {
            var formGroup = $('<div class="form-group"></div>');
            formGroup.append('<label for="' + field.id + '">' + field.label + '</label>');
            formGroup.append('<input type="' + field.type + '" class="form-control" id="' + field.id + '" name="' + field.name + '">');
            form.append(formGroup);
        });
        
        $('#entityModal').modal('show');
    }

    // Load entity data for editing
    function loadEntityData(id) {
        $.get(config.listUrl + '?id=' + id, function(response) {
            if (response.result === 'success' && response.data) {
                showEntityModal('Edit Entity');
                var entity = response.data;
                config.formFields.forEach(function(field) {
                    $('#' + field.id).val(entity[field.name]);
                });
            }
        });
    }

    // Save entity (create or update)
    function saveEntity() {
        var formData = {};
        config.formFields.forEach(function(field) {
            formData[field.name] = $('#' + field.id).val();
        });
        
        if (state.currentId) {
            formData.id = state.currentId;
        }

        $.ajax({
            url: state.currentId ? config.updateUrl : config.addUrl,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function(response) {
                if (response.result === 'success') {
                    $('#entityModal').modal('hide');
                    $('#entityTable').bootstrapTable('refresh');
                } else {
                    alert('Operation failed');
                }
            }
        });
    }

    // Delete entity
    function deleteEntity(id) {
        $.get(config.deleteUrl + '?id=' + id, function(response) {
            if (response.result === 'success') {
                $('#entityTable').bootstrapTable('refresh');
            } else {
                alert('Delete failed');
            }
        });
    }

    // Import entities
    function importEntities() {
        var formData = new FormData();
        formData.append('file', $('#importFile')[0].files[0]);

        $.ajax({
            url: config.importUrl,
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                if (response.result === 'success') {
                    $('#importModal').modal('hide');
                    $('#entityTable').bootstrapTable('refresh');
                    alert('Import successful. Total: ' + response.total + ', Available: ' + response.available);
                } else {
                    alert('Import failed');
                }
            }
        });
    }

    // Export entities
    function exportEntities() {
        var params = $.param({
            searchType: state.searchType,
            keyWord: $('#searchKeyWord').val()
        });
        window.location.href = config.exportUrl + '?' + params;
    }

    // Public API
    return {
        init: init
    };

})(jQuery);
