var mongoose = require('mongoose');
var Schema = mongoose.Schema;

const skincareSchema = new mongoose.Schema({
    date: { type: String, required: true },
    note: { type: String, required: true },
    user: {
        type: Schema.Types.ObjectId, ref: 'User'
    }
}, { timestamps: true });

const Skincare = mongoose.model('Skincare', skincareSchema);

module.exports = Skincare